package com.dotmarketing.velocity.directive;

import javax.servlet.http.HttpServletRequest;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.util.PageRequestModeUtil;
import com.liferay.portal.model.User;

public class RenderParams {
  public static final String RENDER_PARAMS_ATTRIBUTE = "com.dotcms.directive.renderparams";
  final boolean live;
  final User user;
  final Language language;
  final Host currentHost;
  final boolean editMode;


  public RenderParams(HttpServletRequest request) {
    live = PageRequestModeUtil.isLive(request);
    user = WebAPILocator.getUserWebAPI().getUser(request);
    language = WebAPILocator.getLanguageWebAPI().getLanguage(request);
    currentHost = WebAPILocator.getHostWebAPI().getHost(request);
    editMode = PageRequestModeUtil.isEditMode(request);
    request.setAttribute(RENDER_PARAMS_ATTRIBUTE, this);



  }



}
