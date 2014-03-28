package tests
import org.scalatest._
import Helpers._

class HelperTest extends FlatSpec with Matchers {
	"Bitstring to value" should "max to max" in {

		val bitarray = Array(1,1,1,1)
		val max = 10;
		val min = -5;


		TrackerHelpers.convertOne(bitarray, max, min) should equal (max)

	}

	"Bitstring to value" should "large max to max" in {

		val bitarray = Array(1,1,1,1,1,1,1,1,1,1)
		val max = 100;
		val min = -5;


		TrackerHelpers.convertOne(bitarray, max, min) should equal (max)

	}

	"Bitstring to value" should "min to min" in {

		val bitarray = Array(0,0,0,0)
		val max = 5;
		val min = -9;


		TrackerHelpers.convertOne(bitarray, max, min) should equal (min)

	}

	"Bitstring to value" should "large min to min" in {

		val bitarray = Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
		val max = 5;
		val min = -99;


		TrackerHelpers.convertOne(bitarray, max, min) should equal (min)

	}

	"Bitstring to value" should "mid to mid" in {

		val bitarray = Array(1,0,0,0)
		val max = 30;
		val min = 0;


		TrackerHelpers.convertOne(bitarray, max, min) should equal (16)

	}
}