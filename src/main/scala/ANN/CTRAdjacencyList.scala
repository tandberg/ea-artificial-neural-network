package ANN

class CTRAdjacencyList(l: List[(CTRNeuron, CTRNeuron, Double)]){
	def this() = this(List())
		val list = l
	def add(link: (CTRNeuron, CTRNeuron, Double)): CTRAdjacencyList = new CTRAdjacencyList(this.list :+ link) 
	def add(links: List[(CTRNeuron, CTRNeuron, Double)]):CTRAdjacencyList = new CTRAdjacencyList(this.list ::: links)
	
	override def toString(): String = this.list.toString

	def weights:List[Double] = this.list map { 
		case (neuron1, neuron2, weight) => weight
	}

	def linkFromCTRNeuron(neuron: CTRNeuron):List[(CTRNeuron,CTRNeuron, Double)] = 
		this.list.filter {
			case (neuron1, neuron2, weight) => {
				if (neuron1 == neuron) true
				else false
			}

		} 
	def linksToCTRNeuron(neuron: CTRNeuron): List[(CTRNeuron, CTRNeuron, Double)] = {
		this.list.filter {
			case (neuron1, neuron2, weight) => {
				if (neuron2 == neuron) true
				else false
			}
		}
	}
}

