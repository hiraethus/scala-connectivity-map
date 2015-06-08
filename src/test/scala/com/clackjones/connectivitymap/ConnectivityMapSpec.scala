package com.clackjones.connectivitymap
import ConnectivityMap._
import com.clackjones.connectivitymap.referenceprofile.ReferenceProfile

class ConnectivityMapSpec extends UnitSpec {
  "A single gene expression profile and query signature" should "correctly calculate a single connection score" in {
    val expressionProfile = new ReferenceProfile(name = "profile1", Map("gene1" -> 1, "gene2" -> -5, "gene3" -> 7))
    val querySignature : QuerySignature = Map("gene2" -> 1, "gene3" -> 1)

    connectionStrength(expressionProfile, querySignature) shouldBe (expressionProfile.name, 2)
  }

  "connectionScore" should "return a connection strength tuple with score 1.0 when maxConnectionStrength has the same strength value" in {

    val expressionProfile = new ReferenceProfile(name = "profile1", Map("gene1" -> 1, "gene2" -> -5, "gene3" -> 7))
    val querySignature : QuerySignature = Map("gene2" -> 1, "gene3" -> 1)

    val strength = 50f

    def mockConnectionStrength(prof: ReferenceProfile, query: QuerySignature): (String, Float) =
      ("resultprofile", strength)

    val maxConnectionStrength: Float = strength

    connectionScore(expressionProfile, querySignature,
      mockConnectionStrength, maxConnectionStrength) shouldBe ("resultprofile", 1f)
  }

  it should "return one connection strength tuple with score 0.5 when maxConnectionStrength has twice strength value" in {

    val expressionProfile = new ReferenceProfile(name = "profile1", Map("gene1" -> 1, "gene2" -> -5, "gene3" -> 7))
    val querySignature : QuerySignature = Map("gene2" -> 1, "gene3" -> 1)

    val strength = 50

    def mockConnectionStrength(prof: ReferenceProfile, query: QuerySignature): (String, Float) =
      ("resultprofile", strength)

    val maxConnectionStrength: Float = strength * 2

    connectionScore(expressionProfile, querySignature,
      mockConnectionStrength, maxConnectionStrength) shouldBe ("resultprofile", 0.5f)
  }
}