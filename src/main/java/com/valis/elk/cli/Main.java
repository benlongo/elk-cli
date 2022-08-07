package com.valis.elk.cli;

import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.json.ElkGraphJson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Main {
    private static final IGraphLayoutEngine layoutEngine = new RecursiveGraphLayoutEngine();

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        var input = reader.lines().collect(Collectors.joining("\n"));

        var graph = ElkGraphJson.forGraph(input).toElk();

        layoutEngine.layout(graph, new NullElkProgressMonitor());

        var exportedGraphJson = ElkGraphJson.forGraph(graph)
                .omitUnknownLayoutOptions(true)
                .prettyPrint(true)
                .toJson();

        System.out.print(exportedGraphJson);
    }
}
