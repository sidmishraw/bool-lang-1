/*
 * Copyright (c) 2017. Sidharth Mishra. All rights reserved. Please contact me before using my code. It may destroy your machine otherwise. JK!
 */

package boollang


/**
  * Project:        BoolLang1
  * Package:        boollang
  *
  * @author sidmishraw
  *
  *
  *         Description: BigStepSemantics for a sample language BOOL*
  *
  *         e :=    True
  *         |   False
  *         |   if e then e else e
  *
  *
  *         Last modified:  9/18/17 9:00 PM
  */
object BigStepSemantic {
    
    /**
      * Evaluates the expression
      *
      * @param e the expression
      * @return the value of the evaluated expression
      */
    def evaluate(e: E): V = {
        
        e match {
            
            case ETrue() => VTrue()
            
            case EFalse() => VFalse()
            
            case Eif(e1, e2, e3) => if (evaluate(e1).value) {
                
                evaluate(e2)
            } else {
                
                evaluate(e3)
            }
        }
    }
    
    /**
      * The main driver
      *
      * @param args command args
      */
    def main(args: Array[String]): Unit = {
        
        // test cases
        val e1 = Eif(ETrue(), EFalse(), ETrue())
        val e2 = ETrue()
        val e3 = EFalse()
        val e4 = Eif(Eif(ETrue(), EFalse(), ETrue()), Eif(EFalse(), EFalse(), ETrue()), Eif(ETrue(), EFalse(), EFalse()))
        
        
        println(s"e1 :: ${e1.show} evaluates to :: ${evaluate(e1).show}")
        println(s"e2 :: ${e2.show} evaluates to :: ${evaluate(e2).show}")
        println(s"e3 :: ${e3.show} evaluates to :: ${evaluate(e3).show}")
        println(s"e4 :: ${e4.show} evaluates to :: ${evaluate(e4).show}")
    }
    
    /**
      * The expression
      */
    abstract class E {
        
        def show: String
    }
    
    /**
      * Values
      */
    abstract class V {
        
        def value: Boolean
        
        def show: String
    }
    
    case class Eif(e1: E, e2: E, e3: E) extends E {
        
        override def show: String = "\"" + s"if ${e1.show} then ${e2.show} else ${e3.show}" + "\""
    }
    
    case class ETrue() extends E {
        
        override def show: String = "ETrue"
    }
    
    /**
      * The Expression False
      */
    case class EFalse() extends E {
        
        override def show: String = "EFalse"
    }
    
    /**
      * Represents the True value
      *
      * @param value the boolean true
      */
    case class VTrue(value: Boolean = true) extends V {
        
        /**
          * Gives you "True"
          */
        override def show: String = "VTrue"
    }
    
    /**
      * Represents the value "False"
      *
      * @param value the boolean false
      */
    case class VFalse(value: Boolean = false) extends V {
        
        /**
          * Gives you "False"
          */
        override def show: String = "VFalse"
    }
    
}
