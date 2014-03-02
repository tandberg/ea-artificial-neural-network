package tests
import org.scalatest._
import ANN.ANN

class SumTest extends FlatSpec with Matchers {
	"sum" should "sum 1 + 2 to 3" in {
		ANN.sum(1,2) should equal (3)
	}
}