package com.valis.elk.nativeapi;

import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.util.NullElkProgressMonitor;
import org.eclipse.elk.graph.json.ElkGraphJson;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CIntPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.word.UnsignedWord;

import java.nio.charset.StandardCharsets;

public class Main {
    private static final IGraphLayoutEngine layoutEngine = new RecursiveGraphLayoutEngine();

    private static String Layout(String rawGraphJson) {
        var graph = ElkGraphJson.forGraph(rawGraphJson).toElk();
        layoutEngine.layout(graph, new NullElkProgressMonitor());
        return ElkGraphJson.forGraph(graph).omitUnknownLayoutOptions(true).prettyPrint(true).toJson();
    }

    static String result = null;


    @CEntryPoint(name = "Java_com_valis_elk_nativeapi_Layout")
    public static int NativeLayout(CCharPointer cRawGraphJson, CIntPointer sizeOut, @CEntryPoint.IsolateThreadContext IsolateThread isolateThread) {
        try {
            String inputRawGraphJson = CTypeConversion.toJavaString(cRawGraphJson);
            result = Layout(inputRawGraphJson);
            var numBytes = result.getBytes(StandardCharsets.UTF_8).length;
            sizeOut.write(numBytes);
            return 0;
        } catch (Exception ignored) {
            return -1;
        }
    }


    @CEntryPoint(name = "Java_com_valis_elk_nativeapi_CopyResult")
    public static UnsignedWord NativeCopyResult(CCharPointer buffer, UnsignedWord size, @CEntryPoint.IsolateThreadContext IsolateThread isolateThread) {
        var bytesWritten = CTypeConversion.toCString(result, buffer, size);
        buffer.write((int) bytesWritten.rawValue(), (byte) 0); // Null terminate it
        result = null;
        return bytesWritten;
    }
}
