package ANN

class AdjacencyList(l: List[(Neuron, Neuron, Double)]){
	def this() = this(List())
		val list = l
	def add(link: (Neuron, Neuron, Double)): AdjacencyList = new AdjacencyList(this.list :+ link) 
	def add(links: List[(Neuron, Neuron, Double)]):AdjacencyList = new AdjacencyList(this.list ::: links)
	
	override def toString(): String = this.list.toString

	def weights:List[Double] = this.list map { 
		case (neuron1, neuron2, weight) => weight
	}

	def linkFromNeuron(neuron: Neuron):List[(Neuron,Neuron, Double)] = 
		this.list.filter {
			case (neuron1, neuron2, weight) => {
				if (neuron1 == neuron) true
				else false
			}

		} 
	def linksToNeuron(neuron: Neuron): List[(Neuron, Neuron, Double)] = {
		this.list.filter {
			case (neuron1, neuron2, weight) => {
				if (neuron2 == neuron) true
				else false
			}
		}
	}
}

object AdjacencyListTest {



}
