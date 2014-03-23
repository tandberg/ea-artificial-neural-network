package tests
import org.scalatest._
import Flatland.Agent

class AgentTest extends FlatSpec with Matchers {
	"bitsToNumbers" should "do it right" in {

		val bitarray = Array(0,0,0,1,1,1,1,1,1,0,0,1)

		val a = new Agent
		val weights = a.bitStringToWeights(bitarray)
		weights == List(0.0625, 0.9375, 0.5625)
	}
}