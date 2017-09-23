# The WHILE language

This is the language implementation for **`WHILE`** language using Scala programming language.

This language unlike BOOL and BOOL++, supports while loop and mutable state.

## Language Specification:

```
e ::= x
    | v
    | x := e
    | e; e
    | e op e
    | if e then e else e 
    | while (e) e
    | not e
```

```
v ::= True
    | False
    | n
    
op := +
    | -
    | *
    | /
    | >
    | <
    | <=
    | >=
    | and
    | or
```

## Small Step Operational Semantics

```
[SS-VALUE]               -----------------------------
                            v, σ -> v, σ


                            x ∈ domain(σ)  σ(x) = v
[SS-VAR]                 -----------------------------
                            x, σ -> v, σ


[SS-ASSIGN-REDUCTION]    -----------------------------
                            x := v, σ -> v, σ[x:=v]


                            e, σ -> e', σ'
[SS-ASSIGN-CONTEXT]      -----------------------------
                            x := e, σ -> x := e', σ'


[SS-SEQ-REDUCTION]       -----------------------------
                            v; e , σ -> e, σ


                            e1, σ -> e1', σ'
[SS-SEQ-CONTEXT]         -----------------------------
                            e1; e2, σ -> e1'; e2, σ'


[SS-IF-TRUE-REDUCTION]   --------------------------------------
                            if True then e1 else e2, σ -> e1, σ


[SS-IF-FALSE-REDUCTION]  --------------------------------------
                            if False then e1 else e2, σ -> e2, σ


                                e1, σ -> e1', σ'
[SS-IF-CONTEXT]          -----------------------------------------------------------
                            if e1 then e2 else e3, σ -> if e1' then e2 else e3, σ'


                                v = apply op v1 v2
[SS-OP-REDUCTION]        ---------------------------------------------------
                               v1 op v2, σ -> v, σ


                                e, σ -> e', σ'
[SS-OP-CONTEXT#2]        ---------------------------------------------------
                               v op e, σ -> v op e', σ'


                                e1, σ -> e1', σ'
[SS-OP-CONTEXT#1]        ---------------------------------------------------
                               e1 op e2, σ -> e1' op e2, σ'


[SS-WHILE]               ------------------------------------------------------------------
                            while(e1) e2, σ -> if e1 then (e2; while(e1) e2) else false, σ

                            
[SS-NOT-TRUE]            -----------------------------------   
                            not True, σ -> False, σ


[SS-NOT-FALSE]           -----------------------------------   
                            not False, σ -> True, σ


                            e, σ -> e', σ'
[SS-NOT-CONTEXT]         -----------------------------------
                            not e, σ -> not e', σ'
```