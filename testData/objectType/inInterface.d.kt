package inInterface

native
public trait `T$0` {
    public fun bar(a: Any): Number
    public var baz: Any? = noImpl
    public var boo: Any? = noImpl
    public var show: (overrideChecks: Boolean) -> Unit
}
native
public trait `T$1` {
    public var value: Any? = noImpl
    public var done: Boolean
}
native
public trait `T$2` {
    public fun bar(a: Any): Number
    public fun baz(a: Any, b: Any, c: String): Boolean
}
native
public trait Foo {
    public fun withObjectTypeParam(opt: `T$0`): Unit
    public fun returnsObjectType(): `T$1`
    public var foo: `T$2`
}
