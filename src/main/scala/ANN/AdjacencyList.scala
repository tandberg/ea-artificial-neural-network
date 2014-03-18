package ANN
import ANN.Neuron
class AdjacencyList(l: List[(Neuron, Neuron, Double)]){
	def this() = this(List())
		val list = l
	def add(link: (Neuron, Neuron, Double)): AdjacencyList = new AdjacencyList(this.list :+ link) 
	def add(links: List[(Neuron, Neuron, Double)]):AdjacencyList = new AdjacencyList(this.list ::: links)
	
	override def toString(): String = this.list.toString

	def weights:List[Double] = this.list map { 
		case (neuron1, neuron2, weight) => weight
	}
}

object AdjacencyListTest {



}
