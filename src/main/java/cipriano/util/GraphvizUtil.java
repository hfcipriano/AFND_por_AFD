package cipriano.util;

import cipriano.Principal;
import cipriano.model.Transicao;
import cipriano.util.Enums.EstadoEnum;
import com.sun.javaws.exceptions.InvalidArgumentException;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizException;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.Node;
import javafx.stage.FileChooser;

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

        Graph digraph = graph("G").directed();
        digraph.general().attr("rankdir", "LR");
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

            Link link = to(para).attr("label", transicao.getCaractere());
            digraph = digraph.node(de.link(link));

            if(transicao.getAtual().getEstado().equals(EstadoEnum.FINAL) || transicao.getAtual().getEstado().equals(EstadoEnum.AMBOS)){
                de = de.attr(Shape.DOUBLE_CIRCLE);
            }
            if(inicio != null){
                digraph = digraph.node(inicio.link(to(de)));
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha o local de salvamento do arquivo");
        fileChooser.setInitialFileName("grafo_AFD.png");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        try {
            Graphviz.fromGraph(digraph).renderToFile(fileChooser.showSaveDialog(null));
        }catch (RuntimeException e){
            throw new GraphvizException("Arquivo não escolhido");
        }
    }
}
