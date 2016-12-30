package com.cts.spring.boot.Main;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.cts.spring.boot.Main"}) 
public class LdapConfig {

	@Bean
	public LdapContextSource getContextSource() throws Exception{
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl("ldap://ldap.forumsys.com:389");
		ldapContextSource.setBase("dc=example,dc=com");
		ldapContextSource.setUserDn("read-only-admin");
		ldapContextSource.setPassword("password");
		return ldapContextSource;
	}
	
	@Bean
	public LdapTemplate ldapTemplate() throws Exception {
		LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
		ldapTemplate.setIgnorePartialResultException(true);
		ldapTemplate.setContextSource(getContextSource());
		return ldapTemplate;
	}
}