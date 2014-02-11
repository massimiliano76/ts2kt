/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package ts2kt

import js.debug.console

native
fun require(name: String): Any = noImpl

//val Kotlin = require("./kotlin")
//val SyntaxWalker = require("./SyntaxWalker");

val SRC_FILE_PATH_ARG_INDEX = 2;
val OUT_FILE_PATH_ARG_INDEX = 3;
val TYPESCRIPT_DEFINITION_FILE_EXT = ".d.ts";

fun translate(srcPath: String): String {
    val typescript = require("../../../script/typescript") as typescript.typescript;

    val compiler = typescript.TypeScriptCompiler(typescript.DiagnosticsLogger(typescript.IO));
    val batch = typescript.BatchCompiler(typescript.IO);
    val sourceFile = batch.getSourceFile(srcPath);
    compiler.addSourceUnit(srcPath, sourceFile.scriptSnapshot, sourceFile.byteOrderMark, 0, false);

    val tree = compiler.getSyntaxTree(srcPath);
    val typeScriptToKotlinWalker = TypeScriptToKotlinWalker();
    tree.sourceUnit().accept(typeScriptToKotlinWalker);

    val path = require("path") as node.path
    val srcName = path.basename(srcPath, TYPESCRIPT_DEFINITION_FILE_EXT)

    var out = "package " + srcName + "\n\n" + typeScriptToKotlinWalker.out;

    return out
}

fun translateToFile(srcPath: String, outPath: String) {
    val fs = require("fs") as node.fs
    fs.writeFileSync(outPath, translate(srcPath));
}

native
object module {
    val parent: Any? = null
}

fun main(args: Array<String>) {
    if (module.parent != null) return

    val process = require("process") as node.process

    val srcPath = process.argv[SRC_FILE_PATH_ARG_INDEX];
    val outPath = process.argv[OUT_FILE_PATH_ARG_INDEX];

    console.log(srcPath);
    console.log(outPath);

    if (srcPath == null || outPath == null) return

    translateToFile(srcPath, outPath);

//    typescript.Bar
//    typescript.Foo
//
//    typescript.A()
//    typescript.B()
//    typescript.C()
//    typescript.D()
}

/*
var Kotlin = require("../../../script/kotlin");var SyntaxWalker = require("../../../script/SyntaxWalker");
module.exports = Kotlin.modules.ts2kt.ts2kt;
*/