include BuildSupport
BuildSupport::BUILD_DEBUG = true

BUILD_CONFIG = {
  :directory => "../tempo.server",
  
  # build with ode, axis2, uifw
  #
  # :mode => [BuildMode::BPMS,BuildMode::UIFW],
  
  #
  # build with ode, axis2, ui-fw, cas and ldap
  :mode => [BuildMode::BPMS,BuildMode::UIFW, BuildMode::CAS, BuildMode::LDAP],
  
  # build an opensso server
  #
  # :mode =>  [BuildMode::TOMCAT6,BuildMode::OPENSSO_SERVER],
  #
  
  # build with ode, axis2, uifw, ldap and cas
  # :mode => [BuildMode::BPMS,BuildMode::UIFW, BuildMode::CAS],
  
  # build standalone cas server
  # :mode => [BuildMode::TOMCAT5, BuildMode::CAS],
  
  ### BELOW IS NOT TESTED SO MUCH
  
  # build with ode, axis2, *NO* uifw, and opensso support on the server side
  #
  # :mode => [BuildMode::BPMS,BuildMode::OPENSSO]
  #
  # build full tempo with CAS server, but w/o uifw
  #
  # :mode => [BuildMode::BPMS,BuildMode::CAS, BuildMode::LDAP],
  #
  #
  #
  # build the above and zip the resulting folder
  # :mode => [BuildMode::TOMCAT,BuildMode::UIFW,BuildMode::ZIP],
  # :mode => [BuildMode::TOMCAT,BuildMode::UIFW],
  
  # build with tomcat6 and uifw 
  #
  # :mode => [BuildMode::TOMCAT6,BuildMode::UIFW],
  #
  
  # build with tomcat6 and uifw, enable for opensso 
  #
  # :mode => [BuildMode::REMOTE, BuildMode::TOMCAT6,BuildMode::TOKEN_SERVICE, BuildMode::UIFW,BuildMode::OPENSSO,BuildMode::AGENT],
  #
  
  # build with liferay and uifw
  #
  # :mode => [BuildMode::LIFERAY, BuildMode::UIFW, BuildMode::CAS, BuildMode::RELEASE],
  #
  
  # build with jetty and ui-fw along with CAS and LDAP
  #
  # :mode => [BuildMode::JETTY, BuildMode::UIFW, BuildMode::CAS, BuildMode::LDAP],
  #
  
  :ode => :v1_3_4_snapshot,
  :tomcat => :v5,
  :jetty => :v7,
  :osgi_jetty => :v7,
  #:liferay => {:v => :v5_2_5ee, :base_folder => "liferay-portal-5.2.5", :server_folder => "tomcat-5.5.27"},
  :liferay => {:v => :v5_2_3, :base_folder => "liferay-portal-5.2.3", :server_folder => "tomcat-5.5.27"},
  #:liferay => {:v => :v5_2_5_jbee, :base_folder => "liferay-portal-5.2.5", :server_folder => "jboss-tomcat-4.2.3"},  
  :alfresco => :v3_0,
  :tempo => {
    # :core => "6.0.0.75", 
    # TODO: version after 6.0.0.84 include the OpenJPA fix 
    # and do not run on windows, see https://issues.apache.org/jira/browse/OPENJPA-1410
    :core => "6.0.4.011",
    :security => "1.0.16",
    :deploy => "1.0.29",
    
    :processes => "6.0.12",
    :formManager => "6.0.0.48",
    :apacheds => "6.0.0.37",
    :cas => "6.0.0.37"
  }
}
