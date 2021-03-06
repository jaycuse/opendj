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
 * Copyright 2006-2009 Sun Microsystems, Inc.
 * Portions Copyright 2014-2016 ForgeRock AS.
 */
package org.opends.server.extensions;

import static org.opends.messages.ExtensionMessages.ERR_SMTPALERTHANDLER_NO_SMTP_SERVERS;
import static org.opends.messages.ExtensionMessages.WARN_SMTPALERTHANDLER_ERROR_SENDING_MESSAGE;
import static com.forgerock.opendj.cli.Utils.wrapText;

import java.util.ArrayList;
import java.util.List;

import org.forgerock.i18n.LocalizableMessage;
import org.forgerock.i18n.slf4j.LocalizedLogger;
import org.forgerock.opendj.config.server.ConfigException;
import org.forgerock.opendj.config.server.ConfigurationChangeListener;
import org.forgerock.opendj.server.config.server.AlertHandlerCfg;
import org.forgerock.opendj.server.config.server.SMTPAlertHandlerCfg;
import org.opends.server.api.AlertGenerator;
import org.opends.server.api.AlertHandler;
import org.opends.server.core.DirectoryServer;
import org.forgerock.opendj.config.server.ConfigChangeResult;
import org.opends.server.types.InitializationException;
import org.opends.server.util.EMailMessage;

/**
 * This class implements a Directory Server alert handler that may be used to
 * send administrative alerts via SMTP.
 */
public class SMTPAlertHandler
       implements AlertHandler<SMTPAlertHandlerCfg>,
                  ConfigurationChangeListener<SMTPAlertHandlerCfg>
{
  private static final LocalizedLogger logger = LocalizedLogger.getLoggerForThisClass();

  /** The current configuration for this alert handler. */
  private SMTPAlertHandlerCfg currentConfig;

  /** Creates a new instance of this SMTP alert handler. */
  public SMTPAlertHandler()
  {
    super();

    // All initialization should be done in the initializeAlertHandler method.
  }

  @Override
  public void initializeAlertHandler(SMTPAlertHandlerCfg configuration)
       throws ConfigException, InitializationException
  {
    // Make sure that the Directory Server is configured with information about
    // at least one SMTP server.
    if (DirectoryServer.getCoreConfigManager().getMailServerPropertySets() == null ||
        DirectoryServer.getCoreConfigManager().getMailServerPropertySets().isEmpty())
    {
      throw new ConfigException(ERR_SMTPALERTHANDLER_NO_SMTP_SERVERS.get());
    }

    configuration.addSMTPChangeListener(this);
    currentConfig = configuration;
  }

  @Override
  public AlertHandlerCfg getAlertHandlerConfiguration()
  {
    return currentConfig;
  }

  @Override
  public boolean isConfigurationAcceptable(AlertHandlerCfg configuration,
                                           List<LocalizableMessage> unacceptableReasons)
  {
    return true;
  }

  @Override
  public void finalizeAlertHandler()
  {
    // No action is required.
  }

  @Override
  public void sendAlertNotification(AlertGenerator generator, String alertType,
                                    LocalizableMessage alertMessage)
  {
    SMTPAlertHandlerCfg cfg = currentConfig;

    ArrayList<String> recipients = new ArrayList<>(cfg.getRecipientAddress());

    String alertIDStr;
    String alertMessageStr;
    if (alertMessage != null) {
      alertIDStr = alertMessage.resourceName() + "-" + alertMessage.ordinal();
      alertMessageStr = alertMessage.toString();
    } else {
      alertIDStr = "-1";
      alertMessageStr = "none";
    }
    String subject = replaceTokens(cfg.getMessageSubject(), alertType,
                                   alertIDStr, alertMessageStr);

    String body = replaceTokens(cfg.getMessageBody(), alertType, alertIDStr,
                                alertMessageStr);

    EMailMessage message = new EMailMessage(cfg.getSenderAddress(), recipients,
                                            subject);

    message.setBody(LocalizableMessage.raw(wrapText(body, 75)));

    try
    {
      message.send();
    }
    catch (Exception e)
    {
      logger.traceException(e);
      logger.error(WARN_SMTPALERTHANDLER_ERROR_SENDING_MESSAGE,
          alertType, alertMessage, e.getLocalizedMessage());
    }
  }

  /**
   * Replaces any occurrences of special tokens in the given string with the
   * appropriate value.  Tokens supported include:
   * <UL>
   *   <LI>%%alert-type%% -- Will be replaced with the alert type string</LI>
   *   <LI>%%alert-id%% -- Will be replaced with the alert ID value</LI>
   *   <LI>%%alert-message%% -- Will be replaced with the alert message</LI>
   *   <LI>\n -- Will be replaced with an end-of-line character.
   * </UL>
   *
   * @param  s             The string to be processed.
   * @param  alertType     The string to use to replace the "%%alert-type%%"
   *                       token.
   * @param  alertID       The string to use to replace the "%%alert-id%%"
   *                       token.
   * @param  alertMessage  The string to use to replace the "%%alert-message%%"
   *                       token.
   *
   * @return  A processed version of the provided string.
   */
  private String replaceTokens(String s, String alertType, String alertID,
                               String alertMessage)
  {
    return s.replace("%%alert-type%%", alertType).
             replace("%%alert-id%%", alertID).
             replace("%%alert-message%%", alertMessage).
             replace("\\n", "\r\n");
  }

  @Override
  public boolean isConfigurationChangeAcceptable(
                      SMTPAlertHandlerCfg configuration,
                      List<LocalizableMessage> unacceptableReasons)
  {
    return true;
  }

  @Override
  public ConfigChangeResult applyConfigurationChange(
                                 SMTPAlertHandlerCfg configuration)
  {
    currentConfig = configuration;
    return new ConfigChangeResult();
  }
}
