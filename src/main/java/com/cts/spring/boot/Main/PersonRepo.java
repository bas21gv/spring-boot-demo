package com.cts.spring.boot.Main;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepo{
	
	@Autowired
	LdapTemplate ldapTemplate;

	public List<LdapPerson> getAllPerson(){
		return ldapTemplate.search(
		         "", "(objectclass=person)",
		         new AttributesMapper() {
		            public Object mapFromAttributes(Attributes attrs)
		               throws NamingException {
		               return attrs.get("cn").get();
		            }
		         });
	}
}
