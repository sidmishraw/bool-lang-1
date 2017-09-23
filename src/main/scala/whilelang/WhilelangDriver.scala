/*
 * Copyright (c) 2017. Sidharth Mishra. All rights reserved. Please contact me before using my code. It may destroy your machine otherwise. JK!
 */

package whilelang

import scala.collection.mutable

/**
  * Project:        BoolLang1
  * Package:        whilelang
  * Class:          whilelang.WhilelangDriver
  *
  * @author sidmishraw
  *
  *
  *         Description: The WHILE language supports the while loop.
  *
  *         e := a                      // variables/addresses
  *         | v                         // values
  *         | a := e                    // assignment
  *         | e;e                       // sequence
  *         | e op e                    // binary operations
  *         | if e then e else e        // if then else
  *         | while(e) e                // while loops
  *         | not e                     // negation - unary operator
  *
  *         v := True
  *         | False
  *         | n
  *
  *         op := +
  *         | -
  *         | *
  *         | /
  *         | >
  *         | <
  *         | >=
  *         | <=
  *         | and
  *         | or
  *
  *
  *
  *         Last modified:  9/19/17 8:14 AM
  */
object WhilelangDriver {
    
    // this the state store
    val store: mutable.Map[String, ExpValue] = new mutable.HashMap[String, ExpValue]()
    
    /**
      * String to show, a Haskell like Show typeclass
      *
      * @param e the expression
      * @return the string representation of the expression
      */
    def show(e: Exp): String = e match {
    
        case VTrue => "True"
        case VFalse => "False"
        case VInt(i) => s"$i"
    
        case Eif(e1, e2, e3) => s"if ${show(e1)} then ${show(e2)} else ${show(e3)}"
        
        case Ea(name) => name
    
        case :=(a, e) => s"${show(a)} := ${show(e)}"
    
        case ::(e1, e2) => s"${show(e1)}; ${show(e2)}"
        
        case While(e1, e2) => s"while(${show(e1)}) ${show(e2)}"
    
        case Op("+", e1, e2) => s"${show(e1)} + ${show(e2)}"
        case Op("-", e1, e2) => s"${show(e1)} - ${show(e2)}"
        case Op("*", e1, e2) => s"${show(e1)} * ${show(e2)}"
        case Op("/", e1, e2) => s"${show(e1)} / ${show(e2)}"
        case Op(">", e1, e2) => s"${show(e1)} > ${show(e2)}"
        case Op("<", e1, e2) => s"${show(e1)} < ${show(e2)}"
        case Op(">=", e1, e2) => s"${show(e1)} >= ${show(e2)}"
        case Op("<=", e1, e2) => s"${show(e1)} <= ${show(e2)}"
        case Op("and", e1, e2) => s"${show(e1)} and ${show(e2)}"
        case Op("or", e1, e2) => s"${show(e1)} or ${show(e2)}"
    }
    
    /**
      * Evaluates the expression -- lets assume we are using Small step semantics here
      *
      * @param e the expression
      * @return another expression
      */
    def evaluate(e: Exp): Exp = e match {
    
        // SS-VALUE
        // value cases, these are normalized and cannot be processed further
        // no updates to the store
        case VTrue => VTrue
        case VFalse => VFalse
        case VInt(i) => VInt(i)

        // SS-IF-CONDN
        // `if condn context: SS-IF-Context`
        // if e1 then e2 else e3, store -> if e1' then e2 else e3, store'
        // store might get updated when evaluating the conditions
        // SS-IF-True reduction
        // `if True then e1 else e2, store -> e1, store`
        // SS-IF-True reduction
        // `if False then e1 else e2, store -> e2, store`
        case Eif(VTrue, e, _) => e

        case Eif(VFalse, _, e) => e

        case Eif(e1, e2, e3) => Eif(evaluate(e1), e2, e3)

        // SS-VAR
        case Ea(name) => if (store.contains(name)) store(name) else error("not in store")

        // SS-ASSIGN
        // SS-ASSIGN-REDUCTION
        // x := v, store -> v, store(x:=v)
        case :=(a, v: ExpValue) => {
            // store and return
            store(a.name) = v
            v
        }

        // SS-ASSIGN-CONTEXT
        // x := e, store -> x := e', store' || where, e, store -> e', store'
        case :=(a, e) => :=(a, evaluate(e))

        // SS-SEQ
        // SS-SEQ-REDUCTION
        // v; e, store -> e, store
        case ::(_: ExpValue, e) => e

        // SS-SEQ-CONTEXT
        // e1;e2, store -> e1';e2, store' || where e1, store -> e1', store'
        case ::(e1, e2) => ::(evaluate(e1), e2)

        // SS-OP
        // SS-OP-REDUCTION
        case x@Op(s, v1: ExpValue, v2: ExpValue) => x.apply(s, v1, v2)

        // SS-OP-CONTEXT #2
        // v op e, store -> v op e', store' || where e, store -> e', store'
        case Op(s, v: ExpValue, e) => Op(s, v, evaluate(e))

        // SS-OP-CONTEXT #1
        // e1 op e2, store -> e1' op e2, store' || where e1, store -> e1', store'
        case Op(s, e1, e2) => Op(s, evaluate(e1), e2)

        // SS-NOT-REDUCTION-TRUE
        // not True, store -> False, store
        // not False, store -> True, store
        case not(VTrue) => VFalse
        case not(VFalse) => VTrue
        case not(VInt(_)) => error("not supported")

        // SS-NOT-CONTEXT
        // not e, store -> not e', store' || where e, store -> e', store'
        case not(e) => not(evaluate(e))

        //SS-WHILE-CONTEXT
        // while(e1) e2, store -> if e1 then (e2; while(e1) e2) else False, store
        // since the expression has been rewritten as a recursive `if expression`
        // it actually simplifies the expression
        case While(e1, e2) => Eif(e1, ::(e2, While(e1, e2)), VFalse)
    }
    
    /**
      * Process recursively solving the problems
      *
      * @param e the expression to solve
      * @return the value of the expression/program
      */
    def process(e: Exp): ExpValue = e match {
        case x: ExpValue => x
        case x: Exp => {
            println(s"Intermediate step :: ${show(x)}")
            process(evaluate(x))
        }
    }
    
    def main(args: Array[String]): Unit = {
    
        // myVar := 5; kk := 1; while(myVar > 0) (kk := kk + 1; myVar := myVar - 1); kk
        val line1 = :=(Ea("myVar"), VInt(5))
        val line2 = :=(Ea("kk"), VInt(1))
        val line5 = :=(Ea("kk"), Op("+", Ea("kk"), VInt(1)))
        val line6 = :=(Ea("myVar"), Op("-", Ea("myVar"), VInt(1)))
        val line7 = Ea("kk")
        val line4 = ::(line5, line6)
        val line3 = While(Op(">", Ea("myVar"), VInt(0)), line4)
    
        val prog1 = ::(::(::(line1, line2), line3), line7)
    
        val prog2 = ::(:=(Ea("a"), VInt(5)), Op("+", Ea("a"), VInt(10)))
    
        println("---------------------------------------")
        println(s"SHOW :: ${show(prog1)}")
        println("---------------------------------------")
        println(s"Result prog1 :: ${show(process(prog1))}")
        println("---------------------------------------")
    
        println("---------------------------------------")
        println(s"SHOW :: ${show(prog2)}")
        println("---------------------------------------")
        println(s"Result prog2 :: ${show(process(prog2))}")
        println("---------------------------------------")
    }
    
    /**
      * The expression
      */
    abstract class Exp
    
    /**
      * The expression values
      */
    abstract class ExpValue extends Exp
    
    /**
      * Error handler
      *
      * @param msg the message to be displayed for the error
      */
    def error(msg: String): ExpValue = {
        
        throw new Exception(msg)
    }
    
    /**
      * Operations
      *
      * @param e1 left expression
      * @param e2 right expression
      */
    case class Op(s: String, e1: Exp, e2: Exp) extends Exp {
    
        // application of the operation on the operands
        def apply(s: String, v1: ExpValue, v2: ExpValue): ExpValue = {
        
            s match {
            
                case "+" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => VInt(i + j)
                    case _ => error("not supported for the value!")
                }
            
                case "-" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => VInt(i - j)
                    case _ => error("not supported for the value!")
                }
            
                case "*" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => VInt(i * j)
                    case _ => error("not supported for the value!")
                }
            
                case "/" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => VInt(i / j)
                    case _ => error("not supported for the value!")
                }
            
                case ">" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => if (i > j) VTrue else VFalse
                    case _ => error("not supported for the value!")
                }
            
                case ">=" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => if (i >= j) VTrue else VFalse
                    case _ => error("not supported for the value!")
                }
            
                case "<" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => if (i < j) VTrue else VFalse
                    case _ => error("not supported for the value!")
                }
            
                case "<=" => (v1, v2) match {
                    case (VInt(i), VInt(j)) => if (i <= j) VTrue else VFalse
                    case _ => error("not supported for the value!")
                }
            
                case "and" => (v1, v2) match {
                    case (VTrue, VTrue) => VTrue
                    case (_, VFalse) => VFalse
                    case (VFalse, _) => VFalse
                    case (_, _) => error("not supported")
                }
            
                case "or" => (v1, v2) match {
                    case (VFalse, VFalse) => VFalse
                    case (_, VTrue) => VTrue
                    case (VTrue, _) => VTrue
                    case (_, _) => error("not supported")
                }
            }
        }
    }
    
    /**
      * Integer value
      *
      * @param value the integer
      */
    case class VInt(value: Int) extends ExpValue
    
    /**
      * Assignment
      *
      * @param a the address or variable to be assigned to
      * @param e the expression that is assigned
      */
    case class :=(a: Ea, e: Exp) extends Exp
    
    /**
      * Sequence
      *
      * @param e1 first expression
      * @param e2 second expression
      */
    case class ::(e1: Exp, e2: Exp) extends Exp
    
    /**
      * If conditional
      *
      * @param e1 condition
      * @param e2 success
      * @param e3 failure
      */
    case class Eif(e1: Exp, e2: Exp, e3: Exp) extends Exp
    
    /**
      * The variable/address location
      *
      * @param name the name of the variable
      */
    case class Ea(name: String) extends Exp
    
    /**
      * While loop
      *
      * @param e1 the condition
      * @param e2 the while loop body containing expressions that are iterated
      */
    case class While(e1: Exp, e2: Exp) extends Exp
    
    /**
      * True value
      */
    case object VTrue extends ExpValue
    
    /**
      * False value
      */
    case object VFalse extends ExpValue
    
    /**
      * negation
      *
      * @param e the expression being negated
      */
    case class not(e: Exp) extends Exp
}
