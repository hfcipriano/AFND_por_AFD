package cipriano.util;

import cipriano.model.Transicao;
import cipriano.util.Enums.EstadoEnum;
import javagraphviz.src.main.java.com.couggi.javagraphviz.*;

import java.util.*;

/**
 * Created by henrique on 26/08/16.
 */
public class GraphvizUtil {
    public static void gerarGrafo(Set<Transicao> transicaoSet) {
        Map<String, Transicao> transicaoMap = new HashMap<>();
        transicaoSet.forEach(transicao -> transicaoMap.put(transicao.toString(), transicao));

        Digraph digraph = new Digraph("G");
        digraph.attr("rankdir").value("LR");
        transicaoMap.forEach((s, transicao) -> {
            Node inicio = null;
            if(transicao.getAtual().getEstado().equals(EstadoEnum.INICIAL) && !digraph.containsNode(new Node("_start_", digraph))){
                inicio = digraph.addNode("_start_");
                inicio.attr("shape").value("none");
            }
            Node de = digraph.addNode(transicao.getAtual().getNome());
            Node para = digraph.addNode(transicao.getProximoEstadoNome());
            Edge edge = digraph.addEdge(de, para);
            edge.attr("label").value(transicao.getCaractere());

            if(transicao.getAtual().getEstado().equals(EstadoEnum.FINAL) || transicao.getAtual().getEstado().equals(EstadoEnum.AMBOS)){
                de.attr("shape").value("doublecircle");
            }
            if(inicio != null){
                digraph.addEdge(inicio, de);
            }
        });

        GraphvizEngine engine = new GraphvizEngine(digraph);
        engine.addType("png");
        engine.toFilePath("helloworld.png");
        engine.output();
    }
}
