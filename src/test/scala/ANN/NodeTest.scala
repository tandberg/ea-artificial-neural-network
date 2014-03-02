package tests
import org.scalatest._
import ANN.Node

class NodeSumTest extends FlatSpec with Matchers {
	"sumWeights" should "sum 1 + ... + 6 to 21" in {
		val node: Node = new Node()
		node activation (0.1 + 0.3 + 0.6) should equal (0.7310585786300049)
	}
}