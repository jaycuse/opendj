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
 * Copyright 2008 Sun Microsystems, Inc.
 * Portions Copyright 2014-2015 ForgeRock AS.
 */

package org.opends.guitools.controlpanel.datamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opends.guitools.controlpanel.util.Utilities;
import org.forgerock.i18n.LocalizableMessage;
import org.opends.server.types.OpenDsException;

/**
 * The exception that occurs when the user is editing an entry and some of the
 * provided data is not valid.
 *
 */
public class CheckEntrySyntaxException extends OpenDsException
{
  private static final long serialVersionUID = 8145911071581212822L;
  private List<LocalizableMessage> errors;
  /**
   * Constructor of the exception.
   * @param errors the list of error description that were found.
   */
  public CheckEntrySyntaxException(List<LocalizableMessage> errors)
  {
    super(getMessage(errors));
    this.errors = Collections.unmodifiableList(errors);
  }

  /**
   * Returns the list of errors that were encountered.
   * @return the list of errors that were encountered.
   */
  public List<LocalizableMessage> getErrors()
  {
    return errors;
  }

  /**
   * Returns a single message using the provided messages.  This method assumes
   * that the messages have HTML format.
   * @param errors the list of errors.
   * @return a single message using the provided messages.
   */
  private static LocalizableMessage getMessage(List<LocalizableMessage> errors)
  {
    ArrayList<String> s = new ArrayList<>();
    for (LocalizableMessage error : errors)
    {
      s.add(error.toString());
    }
    return LocalizableMessage.raw(Utilities.getStringFromCollection(s, "<br>"));
  }
}
