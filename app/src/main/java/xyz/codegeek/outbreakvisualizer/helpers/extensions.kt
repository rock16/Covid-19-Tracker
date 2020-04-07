package xyz.codegeek.outbreakvisualizer.helpers

/* when(sealedObject) {
    *     is OneType -> //
    *     is AnotherType -> //
    * }.exhaustive
*/

val <T> T.exhaustive: T
    get() = this