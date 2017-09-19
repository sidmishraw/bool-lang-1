# The BOOL* language implementation using Scala

This is the language implementation for **`BOOL*`**

## Language Specification:

```
e ::= True
    | False
    | if e then e else e
```

```
v ::= True
    | False
```

## Big Step operational semantics:

* Rule#1
```
    e1 ⇓ True       e2 ⇓ v
⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯       [BS - IF - True]
if e1 then e2 else e3 ⇓ v
```

* Rule#2
```
    e1 ⇓ False      e3 ⇓ v
⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯      [BS - IF - False]
if e1 then e2 else e3 ⇓ v
```

* Rule#3
```
v ⇓ v
```