package ANN;
import scala.math

class Node {

	def activation(weights: Double*) = {

		def sigmoid(x: Double): Double = {
			1 / (1 + math.exp(-x))
		}

		sigmoid(weights.sum)
	}

}