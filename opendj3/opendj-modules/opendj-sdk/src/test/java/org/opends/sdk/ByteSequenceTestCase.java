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

package org.opends.sdk;



import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.opendj.buildtools.testng.OpenDJTestCase;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



/**
 * Abstract test case for the ByteSequence interface.
 */
public abstract class ByteSequenceTestCase extends OpenDJTestCase
{
  /**
   * ByteSequence data provider that gets the ByteSequence implementation from
   * the abstract method.
   *
   * @return The array of ByteStrings and the bytes it should contain.
   */
  @DataProvider(name = "byteSequenceProvider")
  public Object[][] concreteByteSequenceProvider() throws Exception
  {
    return byteSequenceProvider();
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testByteAt(final ByteSequence bs, final byte[] ba)
  {
    for (int i = 0; i < ba.length; i++)
    {
      Assert.assertEquals(bs.byteAt(i), ba[i]);
    }
  }



  @Test(dataProvider = "byteSequenceProvider", expectedExceptions = IndexOutOfBoundsException.class)
  public void testByteAtBadIndex1(final ByteSequence bs, final byte[] ba)
  {
    bs.byteAt(ba.length);
  }



  @Test(dataProvider = "byteSequenceProvider", expectedExceptions = IndexOutOfBoundsException.class)
  public void testByteAtBadIndex2(final ByteSequence bs, final byte[] ba)
  {
    bs.byteAt(-1);
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testCopyTo(final ByteSequence bs, final byte[] ba)
  {
    final byte[] newBa = new byte[ba.length];
    bs.copyTo(newBa);
    Assert.assertTrue(Arrays.equals(newBa, ba));
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testCopyToByteSequenceBuilder(final ByteSequence bs,
      final byte[] ba)
  {
    final ByteStringBuilder builder = new ByteStringBuilder();
    bs.copyTo(builder);
    Assert.assertTrue(Arrays.equals(builder.toByteArray(), ba));
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testCopyToOutputStream(final ByteSequence bs, final byte[] ba)
      throws Exception
  {
    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bs.copyTo(stream);
    Assert.assertTrue(Arrays.equals(stream.toByteArray(), ba));
  }



  @Test(dataProvider = "byteSequenceProvider", expectedExceptions = IndexOutOfBoundsException.class)
  public void testCopyToWithBadOffset(final ByteSequence bs, final byte[] ba)
  {
    bs.copyTo(new byte[0], -1);
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testCopyToWithOffset(final ByteSequence bs, final byte[] ba)
  {
    for (int i = 0; i < ba.length * 2; i++)
    {
      final byte[] newBa = new byte[ba.length * 2];
      bs.copyTo(newBa, i);

      final byte[] resultBa = new byte[ba.length * 2];
      System.arraycopy(ba, 0, resultBa, i, Math.min(ba.length, ba.length * 2
          - i));
      Assert.assertTrue(Arrays.equals(newBa, resultBa));
    }
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testEquals(final ByteSequence bs, final byte[] ba)
      throws Exception
  {
    Assert.assertTrue(bs.equals(ByteString.wrap(ba)));
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testLength(final ByteSequence bs, final byte[] ba)
      throws Exception
  {
    Assert.assertEquals(bs.length(), ba.length);
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testSubSequence(final ByteSequence bs, final byte[] ba)
  {
    ByteSequence bsSub = bs.subSequence(0, bs.length() / 2);
    byte[] baSub = new byte[ba.length / 2];
    System.arraycopy(ba, 0, baSub, 0, baSub.length);
    Assert.assertTrue(Arrays.equals(bsSub.toByteArray(), baSub));

    bsSub = bs.subSequence(ba.length / 4, (bs.length() / 4) * 3);
    baSub = new byte[(bs.length() / 4) * 3 - ba.length / 4];
    System.arraycopy(ba, ba.length / 4, baSub, 0, baSub.length);
    Assert.assertTrue(Arrays.equals(bsSub.toByteArray(), baSub));

    bsSub = bs.subSequence(ba.length / 2, bs.length());
    baSub = new byte[bs.length() - ba.length / 2];
    System.arraycopy(ba, ba.length / 2, baSub, 0, baSub.length);
    Assert.assertTrue(Arrays.equals(bsSub.toByteArray(), baSub));

    bsSub = bs.subSequence(0, bs.length());
    Assert.assertTrue(Arrays.equals(bsSub.toByteArray(), ba));
  }



  @Test(dataProvider = "byteSequenceProvider", expectedExceptions = IndexOutOfBoundsException.class)
  public void testSubSequenceBadStartEnd1(final ByteSequence bs, final byte[] ba)
  {
    bs.subSequence(-1, bs.length());
  }



  @Test(dataProvider = "byteSequenceProvider", expectedExceptions = IndexOutOfBoundsException.class)
  public void testSubSequenceBadStartEnd2(final ByteSequence bs, final byte[] ba)
  {
    bs.subSequence(0, bs.length() + 1);
  }



  @Test(dataProvider = "byteSequenceProvider", expectedExceptions = IndexOutOfBoundsException.class)
  public void testSubSequenceBadStartEnd3(final ByteSequence bs, final byte[] ba)
  {
    bs.subSequence(-1, bs.length() + 1);
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testToByteArray(final ByteSequence bs, final byte[] ba)
  {
    Assert.assertTrue(Arrays.equals(bs.toByteArray(), ba));
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testToByteSequence(final ByteSequence bs, final byte[] ba)
  {
    Assert.assertTrue(Arrays.equals(bs.toByteString().toByteArray(), ba));
  }



  @Test(dataProvider = "byteSequenceProvider")
  public void testToString(final ByteSequence bs, final byte[] ba)
  {
    String str;
    try
    {
      str = new String(ba, "UTF-8");
    }
    catch (final UnsupportedEncodingException uee)
    {
      str = new String(ba);
    }

    Assert.assertTrue(bs.toString().equals(str));
  }



  protected abstract Object[][] byteSequenceProvider() throws Exception;

}
