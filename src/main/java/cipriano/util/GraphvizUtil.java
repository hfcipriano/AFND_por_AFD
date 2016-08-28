package cipriano.util;

import cipriano.Principal;
import cipriano.model.Transicao;
import cipriano.util.Enums.EstadoEnum;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.to;

/**
 * Created by henrique on 26/08/16.
 */
public class GraphvizUtil {
    public static void gerarGrafo(Set<Transicao> transicaoSet) {
        Map<String, Transicao> transicaoMap = new HashMap<>();
        transicaoSet.forEach(transicao -> transicaoMap.put(transicao.toString(), transicao));

        Graph digraph = graph("G");
        boolean inicioUsed = false;
        for (Transicao transicao : transicaoSet) {
            Node inicio = null;
            if(transicao.getAtual().getEstado().equals(EstadoEnum.INICIAL) && !inicioUsed){
                inicio = node("_start_").attr(Shape.NONE);
                digraph = digraph.node(inicio);
                inicioUsed = true;
            }
            Node de = node(transicao.getAtual().getNome());
            Node para = node(transicao.getProximoEstadoNome());
            digraph = digraph.node(de.link(to(para)));

            if(transicao.getAtual().getEstado().equals(EstadoEnum.FINAL) || transicao.getAtual().getEstado().equals(EstadoEnum.AMBOS)){
                de = de.attr(Shape.DOUBLE_CIRCLE);
            }
            if(inicio != null){
                digraph = digraph.node(inicio.link(to(de)));
            }
        }
        Graphviz.fromGraph(digraph).renderToFile(new File(Principal.class.getResource("/images").getFile().concat("/grafo.png")));
    }
}
