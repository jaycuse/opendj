/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]".
 *
 * Copyright 2006-2008 Sun Microsystems, Inc.
 * Portions Copyright 2014-2016 ForgeRock AS.
 */
package org.opends.server.api;
import org.forgerock.i18n.LocalizableMessage;



import java.util.List;

import org.forgerock.opendj.server.config.server.PasswordGeneratorCfg;
import org.forgerock.opendj.config.server.ConfigException;
import org.opends.server.types.*;
import org.forgerock.opendj.ldap.ByteString;


/**
 * This class defines a set of methods and structures that must be
 * implemented by a Directory Server module that may be used to
 * generate user passwords. The password generator is included as part
 * of a password policy, and is used by the password modify extended
 * operation to construct a new password for the user if that option
 * is chosen.
 *
 * @param  <T>  The type of configuration handled by this password
 *              generator.
 */
@org.opends.server.types.PublicAPI(
     stability=org.opends.server.types.StabilityLevel.UNCOMMITTED,
     mayInstantiate=false,
     mayExtend=true,
     mayInvoke=false)
public abstract class PasswordGenerator
       <T extends PasswordGeneratorCfg>
{
  /**
   * Initializes this password generator based on the information in
   * the provided configuration entry.
   *
   * @param  configuration  The configuration to use to initialize
   *                        this password validator.
   *
   * @throws  ConfigException  If an unrecoverable problem arises in
   *                           the process of performing the
   *                           initialization.
   *
   * @throws  InitializationException  If a problem occurs during
   *                                   initialization that is not
   *                                   related to the server
   *                                   configuration.
   */
  public abstract void initializePasswordGenerator(T configuration)
         throws ConfigException, InitializationException;



  /**
   * Indicates whether the provided configuration is acceptable for
   * this password generator.  It should be possible to call this
   * method on an uninitialized password generator instance in order
   * to determine whether the password generator would be able to use
   * the provided configuration.
   * <BR><BR>
   * Note that implementations which use a subclass of the provided
   * configuration class will likely need to cast the configuration
   * to the appropriate subclass type.
   *
   * @param  configuration        The password generator configuration
   *                              for which to make the determination.
   * @param  unacceptableReasons  A list that may be used to hold the
   *                              reasons that the provided
   *                              configuration is not acceptable.
   *
   * @return  {@code true} if the provided configuration is acceptable
   *          for this password generator, or {@code false} if not.
   */
  public boolean isConfigurationAcceptable(
                      PasswordGeneratorCfg configuration,
                      List<LocalizableMessage> unacceptableReasons)
  {
    // This default implementation does not perform any special
    // validation.  It should be overridden by password generator
    // implementations that wish to perform more detailed validation.
    return true;
  }



  /**
   * Performs any finalization work that may be necessary when this
   * password generator is taken out of service.
   */
  public void finalizePasswordGenerator()
  {
    // No action is performed by default.
  }



  /**
   * Generates a password for the user whose account is contained in
   * the specified entry.
   *
   * @param  userEntry  The entry for the user for whom the password
   *                    is to be generated.
   *
   * @return  The password that has been generated for the user.
   *
   * @throws  DirectoryException  If a problem occurs while attempting
   *                              to generate the password.
   */
  public abstract ByteString generatePassword(Entry userEntry)
         throws DirectoryException;
}

