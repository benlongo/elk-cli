package com.valis.elk.nativeapi;

import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.json.ElkGraphJson;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

public class Main {
    private static final IGraphLayoutEngine layoutEngine = new RecursiveGraphLayoutEngine();

    private static String Layout(String rawGraphJson) {
        var graph = ElkGraphJson.forGraph(rawGraphJson).toElk();
        layoutEngine.layout(graph, new NullElkProgressMonitor());
        return ElkGraphJson.forGraph(graph)
                .omitUnknownLayoutOptions(true)
                .prettyPrint(true)
                .toJson();
    }


    @CEntryPoint(name = "Java_com_valis_elk_nativeapi_Layout")
    public static CCharPointer NativeLayout(CCharPointer cRawGraphJson, IsolateThread isolateThread) {
        String inputRawGraphJson = CTypeConversion.toJavaString(cRawGraphJson);
        var resultRawGraphJson = Layout(inputRawGraphJson);
        var resultPointerHolder = CTypeConversion.toCString(resultRawGraphJson);
        return resultPointerHolder.get();
    }
}
