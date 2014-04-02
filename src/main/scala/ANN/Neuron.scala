package ANN
class Neuron(var activationFunction: (Double) => Double, val label: String, val gain: Double, var timeConstant: Double) {
	var sumOfWeights:Double = 0 
	var y: Double = 0

	def this(label:String) = this(
		(x) => if(0.5 < (1/(1 + math.exp(-x)))) 
			 1
			else 
			0
			, label, 0, 0)

	def this(activationFunction:(Double) => Double, label:String) = this(activationFunction, label, 0, 0)

	def this(activationFunction:(Double) => Double) = this(activationFunction, "", 0, 0)
	def this(label:String, gain:Double, timeConstant:Double) = this(null, label, gain, timeConstant)
	def increaseSumOfWeights(input: Double) = {
		sumOfWeights += input
	}
	def activate() = {
		activationFunction(sumOfWeights)	
	}
	if (gain > 0 && timeConstant > 0){
		activationFunction = (s: Double) => {
			def dy:Double = {
				println(-y + s)
				(1/timeConstant) * (-y + s)
			}
			val returnValue = 1 / (1 + math.exp(-gain * y))
			y += dy
			returnValue
		}
	}
	override def toString = sumOfWeights.toString 
}


