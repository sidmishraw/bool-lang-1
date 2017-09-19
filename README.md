# The BOOL* language implementation using Scala

This is the language implementation for **`BOOL*`**

## Language Specification:

```
e ::= True
    | False
    | n
    | if e then e else e
    | succ e
    | pred e
```

```
v ::= True
    | False
    | n
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

* Rule#4
```
 e ⇓ n
⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯                      [BS - Succ]
succ e ⇓ n + 1
```

* Rule#4
```
 e ⇓ n
⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯                      [BS - Pred]
pred e ⇓ n - 1
```
