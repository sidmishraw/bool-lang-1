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
      * @param e
      * @return
      */
    def show(e: Exp): String = e match {
        case VTrue => "True"
        case VFalse => "False"
        case VInt(i) => s"VInt($i)"
        case Eif(e1, e2, e3) => s"if ${show(e1)} then ${show(e2)} else ${show(e2)}"
        case Ea(name) => name
        case :=(a, e) => s"${show(a)} := ${show(e)}"
        case ::(e1, e2) => s"${show(e1)};${show(e2)}"
        case While(e1, e2) => s"while(${show(e1)}) ${show(e2)}"
        case +(e1, e2) => s"${show(e1)} +  ${show(e2)}"
        case -(e1, e2) => s"${show(e1)} -  ${show(e2)}"
        case *(e1, e2) => s"${show(e1)} *  ${show(e2)}"
        case /(e1, e2) => s"${show(e1)} /  ${show(e2)}"
        case <(e1, e2) => s"${show(e1)} <  ${show(e2)}"
        case >(e1, e2) => s"${show(e1)} >  ${show(e2)}"
        case >=(e1, e2) => s"${show(e1)} <=  ${show(e2)}"
        case <=(e1, e2) => s"${show(e1)} >=  ${show(e2)}"
    }
    
    /**
      * Evaluates the expression -- lets assume we are using BigStep semantics here
      *
      * Caveat - I don't know how it should look in case of small step semantics
      *
      * @param e the expression
      * @return the value of the expression after evaluation
      */
    def evaluate(e: Exp): ExpValue = e match {
        
        // value cases, these are normalized and cannot be processed further
        // no updates to the store(?)
        case VTrue => VTrue
        case VFalse => VFalse
        case VInt(i) => VInt(i)
        
        // if condition
        // store might get updated when evaluating the conditions
        case Eif(e1, e2, e3) => evaluate(e1) match {
            case VTrue => evaluate(e2)
            case VFalse => evaluate(e3)
            case _ => throw new Exception("operation is not supported")
        }
        
        // SS-store-access-reduction
        case Ea(name) => if (store.contains(name)) store(name) else throw new Exception("Not in store")
        
        // SS-assign-context
        case :=(a, e: ExpValue) => {
            // evaluate e to e' and store to store'
            // val ee =
            e
        }
    }
    
    def main(args: Array[String]): Unit = {
        
        val prog1 = ::(:=(Ea("a"), VInt(5)), While(VTrue, WhilelangDriver.+.apply(Ea("a"), VInt(2))))
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
      * Operations
      *
      * @param e1 left expression
      * @param e2 right expression
      */
    abstract class Op(e1: Exp, e2: Exp) extends Exp
    
    /**
      * The variable/address location
      *
      * @param name
      */
    case class Ea(name: String) extends Exp
    
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
      * While loop
      *
      * @param e1 the condition
      * @param e2 the while loop body containing expressions that are iterated
      */
    case class While(e1: Exp, e2: Exp) extends Exp
    
    case class +(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    case class -(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    case class *(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    case class /(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    case class >(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    case class >=(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    case class <(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    case class <=(e1: Exp, e2: Exp) extends Op(e1, e2)
    
    /**
      * True value
      */
    case object VTrue extends ExpValue
    
    /**
      * False value
      */
    case object VFalse extends ExpValue
    
}
