package org.broadinstitute.sting.queue.engine

import java.io.File

/**
 * Utility class to map a set of inputs to set of outputs.
 * The QGraph uses this function internally to map between user defined functions.
 */
class MappingEdge(val inputs: Set[File], val outputs: Set[File]) extends QEdge {
  /**
   * For debugging purposes returns <map>.
   * @return <map>
   */
  override def toString = "<map>"
  override def dotString = "<map>"
}