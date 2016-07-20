package com.dotcms.rest.api.v1.authentication;

import com.dotcms.company.CompanyAPI;
import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotcms.repackage.javax.ws.rs.POST;
import com.dotcms.repackage.javax.ws.rs.Path;
import com.dotcms.repackage.javax.ws.rs.Produces;
import com.dotcms.repackage.javax.ws.rs.core.Context;
import com.dotcms.repackage.javax.ws.rs.core.MediaType;
import com.dotcms.repackage.javax.ws.rs.core.Response;
import com.dotcms.repackage.org.glassfish.jersey.server.JSONP;
import com.dotcms.rest.ResponseEntityView;
import com.dotcms.rest.WebResource;
import com.dotcms.rest.annotation.NoCache;
import static com.dotcms.util.CollectionsUtils.*;

import com.dotcms.rest.api.LanguageView;
import com.dotcms.util.ConversionUtils;
import com.dotcms.util.I18NUtil;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.ApiProvider;
import com.dotmarketing.portlets.languagesmanager.business.LanguageAPI;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.liferay.portal.language.LanguageUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.util.ReleaseInfo;
import com.liferay.util.LocaleUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Encapsulates the necessary info to show the login page.
 * @author jsanca
 */
@Path("/v1/loginform")
public class LoginFormResource implements Serializable {

    private final LanguageAPI languageAPI;
    private final CompanyAPI  companyAPI;
    private final WebResource webResource;
    private final ConversionUtils conversionUtils;
    private final I18NUtil i18NUtil;

    @SuppressWarnings("unused")
    public LoginFormResource() {
        this(I18NUtil.INSTANCE,
                APILocator.getLanguageAPI(),
                ConversionUtils.INSTANCE,
                APILocator.getCompanyAPI(),
                new WebResource(new ApiProvider()));
    }

    @VisibleForTesting
    protected LoginFormResource(final I18NUtil i18NUtil, final LanguageAPI languageAPI,
                                     final ConversionUtils conversionUtils,
                                     final CompanyAPI  companyAPI,
                                     final WebResource webResource) {

        this.i18NUtil        = i18NUtil;
        this.conversionUtils = conversionUtils;
        this.languageAPI     = languageAPI;
        this.companyAPI      = companyAPI;
        this.webResource     = webResource;
    }

    // todo: add the https annotation
    @POST
    @JSONP
    @NoCache
    @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
    public final Response loginForm(@Context final HttpServletRequest request,
                                         final  LoginForm loginForm) {

        Response res = null;

        try {

            final Company defaultCompany =
                    this.companyAPI.getDefaultCompany();

            final LoginFormResultView.Builder builder =
                    new LoginFormResultView.Builder();

            final HttpSession session =
                    request.getSession();

            // try to set to the session the locale company settings
            LocaleUtil.processLocaleCompanySettings(request, session);
            // or the locale user cookie configuration if exits.
            LocaleUtil.processLocaleUserCookie(request, session);

            final Map<String, String> messagesMap =
                    this.i18NUtil.getMessagesMap(
                            // if the user set's a switch, it overrides the session too.
                            loginForm.getCountry(), loginForm.getLanguage(),
                            loginForm.getMessagesKey(), request,
                            true); // want to create a session to store the locale.

            final Locale userLocale = LocaleUtil.getLocale(request,
                    loginForm.getCountry(), loginForm.getLanguage());

            builder.serverId(LicenseUtil.getDisplayServerId())
                .levelName(LicenseUtil.getLevelName())
                .version(ReleaseInfo.getVersion())
                .buildDateString(ReleaseInfo.getBuildDateString())
                .languages(this.conversionUtils.convert(LanguageUtil.getAvailableLocales(),
                        (final Locale locale) -> {

                            return new LanguageView(locale.getLanguage(), locale.getCountry(),
                                    locale.getDisplayName(locale));
                        }))
                .backgroundColor(defaultCompany.getSize())
                .backgroundPicture(defaultCompany.getHomeURL())
                .logo(this.companyAPI.getLogoPath(defaultCompany))
                .authorizationType(defaultCompany.getAuthType())
                .currentLanguage(new LanguageView(userLocale.getLanguage(), userLocale.getCountry(),
                            userLocale.getDisplayName(userLocale)));

            res = Response.ok(new ResponseEntityView(builder.build(), messagesMap)).build(); // 200
        } catch (Exception e) { // this is an unknown error, so we report as a 500.

            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }

        return res;
    } // authentication
} // E:O:F:LoginFormResource.
