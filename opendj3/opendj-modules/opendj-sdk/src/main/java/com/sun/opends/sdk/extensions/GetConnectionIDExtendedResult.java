/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2010 Sun Microsystems, Inc.
 */

package com.sun.opends.sdk.extensions;



import java.io.IOException;

import org.opends.sdk.ByteString;
import org.opends.sdk.ByteStringBuilder;
import org.opends.sdk.ResultCode;
import org.opends.sdk.asn1.ASN1;
import org.opends.sdk.asn1.ASN1Writer;
import org.opends.sdk.responses.AbstractExtendedResult;

import com.sun.opends.sdk.util.Validator;


/**
 * Get connection ID extended result.
 *
 * @see GetConnectionIDExtendedRequest
 */
public final class GetConnectionIDExtendedResult extends
    AbstractExtendedResult<GetConnectionIDExtendedResult>
{
  /**
   * Creates a new get connection ID extended result.
   *
   * @param resultCode
   *          The result code.
   * @param connectionID
   *          The client connection ID.
   * @return The new get connection ID extended result.
   * @throws NullPointerException
   *           If {@code resultCode} was {@code null}.
   */
  public static GetConnectionIDExtendedResult newResult(
      final ResultCode resultCode, final int connectionID)
      throws NullPointerException
  {
    Validator.ensureNotNull(resultCode);
    return new GetConnectionIDExtendedResult(resultCode, connectionID);
  }



  private int connectionID;



  private GetConnectionIDExtendedResult(final ResultCode resultCode,
      final int connectionID)
  {
    super(resultCode);
    this.connectionID = connectionID;
  }



  /**
   * Returns the client connection ID.
   *
   * @return The client connection ID.
   */
  public int getConnectionID()
  {
    return connectionID;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public String getOID()
  {
    return GetConnectionIDExtendedRequest.OID;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public ByteString getValue()
  {
    final ByteStringBuilder buffer = new ByteStringBuilder(6);
    final ASN1Writer writer = ASN1.getWriter(buffer);

    try
    {
      writer.writeInteger(connectionID);
    }
    catch (final IOException ioe)
    {
      // This should never happen unless there is a bug somewhere.
      throw new RuntimeException(ioe);
    }

    return buffer.toByteString();
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValue()
  {
    return true;
  }



  /**
   * Sets the client connection ID.
   *
   * @param connectionID
   *          The client connection ID.
   * @return This get connection ID result.
   */
  public GetConnectionIDExtendedResult setConnectionID(final int connectionID)
  {
    this.connectionID = connectionID;
    return this;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("GetConnectionIDExtendedResponse(resultCode=");
    builder.append(getResultCode());
    builder.append(", matchedDN=");
    builder.append(getMatchedDN());
    builder.append(", diagnosticMessage=");
    builder.append(getDiagnosticMessage());
    builder.append(", referrals=");
    builder.append(getReferralURIs());
    builder.append(", responseName=");
    builder.append(getOID());
    builder.append(", connectionID=");
    builder.append(connectionID);
    builder.append(", controls=");
    builder.append(getControls());
    builder.append(")");
    return builder.toString();
  }
}
