/*
 * Copyright (c) 2017. Sidharth Mishra. All rights reserved. Please contact me before using my code. It may destroy your machine otherwise. JK!
 */

package boollang

/**
  * Project:        BoolLang1
  * Package:        boollang
  * File:           BigStepsForBoolPP
  *
  * @author sidmishraw
  *
  *
  *         Description: The BOOL++ language supports integers and a pred() and succ() functions
  *
  *         e := True
  *         | False
  *         | n
  *         | if e then e else e
  *         | succ e
  *         | pred e
  *
  *         v := True
  *         | False
  *         | n
  *
  *
  *         Last modified:  9/18/17 9:47 PM
  */
object BigStepsForBoolPP {
    
    /**
      * Evaluates the expressoin and returns the result
      *
      * @param e the expression to be evaluated
      * @return the result of the evaluation
      */
    def evaluate(e: E): V = e match {
        case ETrue() => VTrue()
        case EFalse() => VFalse()
        case En(i) => Vn(i)
        case Eif(e1, e2, e3) => evaluate(e1) match {
            case VTrue() => evaluate(e2)
            case VFalse() => evaluate(e3)
            case _ => throw new Exception("Operation is not supported")
        }
        case Succ(e) => evaluate(e) match {
            case Vn(i) => Vn((i + 1))
            case _ => throw new Exception("Operation is not supported")
        }
        case Pred(e) => evaluate(e) match {
            case Vn(i) => Vn((i - 1))
            case _ => throw new Exception("Operation is not supported")
        }
    }
    
    /**
      * Main driver of this language
      *
      * @param args
      */
    def main(args: Array[String]): Unit = {
        
        val prog1 = Eif(ETrue(), ETrue(), EFalse())
        val prog2 = Eif(Eif(ETrue(), EFalse(), ETrue()), ETrue(), Eif(ETrue(), EFalse(), ETrue()))
        val prog3 = Pred(En(5))
        val prog4 = Succ(En(5))
        val prog5 = Pred(Eif(ETrue(), En(50), En(22)))
        
        log(prog1)
        log(prog2)
        log(prog3)
        log(prog4)
        log(prog5)
    }
    
    private def log(e: E): Unit = {
        println(s"Expression :: ${e.show} evaluates to :: ${evaluate(e).show}")
    }
    
    // expressions
    abstract class E {
        def show: String
    }
    
    // values
    abstract class V {
        def show: String
    }
    
    /**
      * The True expression
      */
    case class ETrue() extends E {
        override def show: String = "ETrue"
    }
    
    /**
      * False expression
      */
    case class EFalse() extends E {
        override def show: String = "EFalse"
    }
    
    /**
      * Int expression
      *
      * @param value the int value of the expression
      */
    case class En(value: Int) extends E {
        override def show: String = s"En(${value.toString})"
    }
    
    /**
      * If - then - else conditional
      *
      * @param e1 the if condition
      * @param e2 the then result
      * @param e3 the else result
      */
    case class Eif(e1: E, e2: E, e3: E) extends E {
        override def show: String = s"if ${e1.show} then ${e2.show} else ${e3.show}"
    }
    
    /**
      * Succ gives the successor of the expression
      *
      * @param e the expression
      */
    case class Succ(e: E) extends E {
        override def show: String = s"Succ${e.show}"
    }
    
    /**
      * Pred gives the predecessor of the expression
      *
      * @param e the expression
      */
    case class Pred(e: E) extends E {
        override def show: String = s"Pred(${e.show})"
    }
    
    /**
      * True value
      */
    case class VTrue() extends V {
        override def show: String = "True"
    }
    
    /**
      * False value
      */
    case class VFalse() extends V {
        override def show: String = "False"
    }
    
    /**
      * int value
      *
      * @param value int
      */
    case class Vn(value: Int) extends V {
        override def show: String = s"Vn($value)"
    }
    
}
