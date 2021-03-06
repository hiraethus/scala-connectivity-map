package com.clackjones.connectivitymap.querysignature

import java.io.File
import java.util.regex.Pattern
import scala.io.Source

import com.clackjones.connectivitymap.QuerySignatureMap


trait QuerySignatureLoaderComponent {
  def querySignatureLoader : QuerySignatureLoader

  /**
   * A trait to describe the loading of a query signature (represented by a QuerySignature)
   * from a source
   */
  trait QuerySignatureLoader {

    /**
     * Load an individual query signature
     * @param path the path to the file containing the query signature
     * @return a map containing values mapping a gene's probe ID (String) to whether it should be
     *         upregulated (1) or down-regulated (-1)
     */
    def loadQuerySignature(path: String): Option[QuerySignatureMap]
  }
}

trait QuerySignatureFileLoaderComponent extends QuerySignatureLoaderComponent {
  val querySignatureLoader = new QuerySignatureFileLoader

  class QuerySignatureFileLoader extends QuerySignatureLoader {
    val whitespacePattern = Pattern.compile("\\s+")

    /**
     * Loads an individual query signature from a file.
     * Note: All lines beginning with # are skipped as well as the first line
     * of the file that isn't a comment which should be the heading names
     *
     * @param path the path to the file containing the query signature
     * @return a map containing values mapping a gene's probe ID (String) to whether it should be
     *         upregulated (1) or down-regulated (-1)
     */
    override def loadQuerySignature(path: String): Option[QuerySignatureMap] = {
      if (!(new File(path).exists())) {
        return None
      }
      val srcFile = Source.fromFile(path).getLines() filter (line => line.charAt(0) != '#')

      srcFile.next() // skip titles line

      return Some((srcFile map (line => splitLine(line))).toMap)
    }

    private def splitLine(line: String): (String, Float) = {
      val splitLine = whitespacePattern.split(line.trim())

      val geneName = splitLine(0)
      val geneStrength = splitLine(1)

      (geneName, geneStrength.toFloat)
    }
  }
}