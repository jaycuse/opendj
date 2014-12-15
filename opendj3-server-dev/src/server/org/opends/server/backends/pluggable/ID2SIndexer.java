/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2006-2008 Sun Microsystems, Inc.
 *      Portions Copyright 2014 ForgeRock AS
 */
package org.opends.server.backends.pluggable;

import java.util.*;

import org.forgerock.opendj.ldap.ByteString;
import org.forgerock.opendj.ldap.spi.IndexingOptions;
import org.opends.server.types.Entry;
import org.opends.server.types.Modification;



/**
 * Implementation of an Indexer for the subtree index.
 */
public class ID2SIndexer extends Indexer
{
  /**
   * Create a new indexer for a subtree index.
   */
  public ID2SIndexer()
  {
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return "id2subtree";
  }

  /** {@inheritDoc} */
  @Override
  public void indexEntry(Entry entry, Set<ByteString> addKeys, IndexingOptions options)
  {
    // The superior entry IDs are in the entry attachment.
    ArrayList<EntryID> ids = (ArrayList<EntryID>) entry.getAttachment();

    // Skip the entry's own ID.
    Iterator<EntryID> iter = ids.iterator();
    iter.next();

    // Iterate through the superior IDs.
    while (iter.hasNext())
    {
      ByteString nodeIDData = iter.next().toByteString();
      addKeys.add(nodeIDData);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void replaceEntry(Entry oldEntry, Entry newEntry,
                           Map<ByteString, Boolean> modifiedKeys, IndexingOptions options)
  {
    // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public void modifyEntry(Entry oldEntry, Entry newEntry,
                          List<Modification> mods,
                          Map<ByteString, Boolean> modifiedKeys, IndexingOptions options)
  {
    // Nothing to do.
  }
}