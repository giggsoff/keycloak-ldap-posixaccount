/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.storage.ldap.mappers.membership.group;

import org.apache.commons.collections4.ListUtils;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.ldap.LDAPStorageProvider;
import org.keycloak.storage.ldap.mappers.AbstractLDAPStorageMapper;

import java.util.List;

/**
 * @author Bart Janssens
 */
public class LdapPosixAccountFactory extends GroupLDAPStorageMapperFactory {

    public static final String PROVIDER_ID = "keycloak-ldap-posixaccount";
    protected static final List<ProviderConfigProperty> configProperties;

    static {
        List<ProviderConfigProperty> props = getConfigProps(null);
        configProperties = props;
    }

    static List<ProviderConfigProperty> getConfigProps(ComponentModel p) {

        ProviderConfigurationBuilder config = ProviderConfigurationBuilder.create()
                .property().name(org.keycloak.storage.ldap.mappers.membership.group.LdapPosixAccount.LDAP_NEXT_UID)
                .label("Next POSIX UID")
                .helpText("Value for the POSIX UID number for the next new user. This should not refer to any existing user and will be incremented automatically with each newly added user.")
                .type(ProviderConfigProperty.STRING_TYPE)
                .add();
        return config.build();
    }

    @Override
    public String getHelpText() {
        return "Used to assign an auto-incrementing POSIX account UID number (LDAP attribute uidNumber) to newly created LDAP users. Also sets homeDirectory to /home/username. Also map groups and set GID numbers.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ListUtils.union(super.getConfigProperties(), configProperties);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        super.validateConfiguration(session, realm, config);
        checkMandatoryConfigAttribute(org.keycloak.storage.ldap.mappers.membership.group.LdapPosixAccount.LDAP_NEXT_UID, "Next POSIX UID", config);
    }

    @Override
    protected AbstractLDAPStorageMapper createMapper(ComponentModel mapperModel, LDAPStorageProvider federationProvider) {
        return new org.keycloak.storage.ldap.mappers.membership.group.LdapPosixAccount(mapperModel, federationProvider, this);
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties(RealmModel realm, ComponentModel parent) {
        return ListUtils.union(super.getConfigProperties(), getConfigProps(parent));
    }
}
