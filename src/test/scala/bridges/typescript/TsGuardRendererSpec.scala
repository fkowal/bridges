package bridges.typescript

import bridges.SampleTypes._
import bridges.typescript.TsType._
import bridges.typescript.syntax._
import org.scalatest._
import unindent._

class TsGuardRendererSpec extends FreeSpec with Matchers {
  "Color" in {
    TypescriptGuard.render(decl[Color]) shouldBe {
      i"""
      export const isColor = (v: any): v is Color => {
        return typeof v === "object" && v != null && "red" in v && typeof v.red === "number" && "green" in v && typeof v.green === "number" && "blue" in v && typeof v.blue === "number";
      }
      """
    }
  }

  "Circle" in {
    TypescriptGuard.render(decl[Circle]) shouldBe {
      i"""
      export const isCircle = (v: any): v is Circle => {
        return typeof v === "object" && v != null && "radius" in v && typeof v.radius === "number" && "color" in v && isColor(v.color);
      }
      """
    }
  }

  "Rectangle" in {
    TypescriptGuard.render(decl[Rectangle]) shouldBe {
      i"""
      export const isRectangle = (v: any): v is Rectangle => {
        return typeof v === "object" && v != null && "width" in v && typeof v.width === "number" && "height" in v && typeof v.height === "number" && "color" in v && isColor(v.color);
      }
      """
    }
  }

  "Shape" in {
    TypescriptGuard.render(decl[Shape]) shouldBe {
      i"""
      export const isShape = (v: any): v is Shape => {
        return typeof v === "object" && v != null && "type" in v && (v.type === "Circle" ? typeof v === "object" && v != null && "radius" in v && typeof v.radius === "number" && "color" in v && isColor(v.color) : v.type === "Rectangle" ? typeof v === "object" && v != null && "width" in v && typeof v.width === "number" && "height" in v && typeof v.height === "number" && "color" in v && isColor(v.color) : v.type === "ShapeGroup" ? typeof v === "object" && v != null && "leftShape" in v && isShape(v.leftShape) && "rightShape" in v && isShape(v.rightShape) : false);
      }
      """
    }
  }

  "Alpha" in {
    TypescriptGuard.render(decl[Alpha]) shouldBe {
      i"""
      export const isAlpha = (v: any): v is Alpha => {
        return typeof v === "object" && v != null && "name" in v && typeof v.name === "string" && "char" in v && typeof v.char === "string" && "bool" in v && typeof v.bool === "boolean";
      }
      """
    }
  }

  "ArrayClass" in {
    TypescriptGuard.render(decl[ArrayClass]) shouldBe {
      i"""
      export const isArrayClass = (v: any): v is ArrayClass => {
        return typeof v === "object" && v != null && "aList" in v && Array.isArray(v.aList) && v.aList.map((i: any) => typeof i === "string").reduce((a: any, b: any) => a && b) && (!("optField" in v) || typeof v.optField === "number" || v.optField === null);
      }
      """
    }
  }

  "Numeric" in {
    TypescriptGuard.render(decl[Numeric]) shouldBe {
      i"""
      export const isNumeric = (v: any): v is Numeric => {
        return typeof v === "object" && v != null && "double" in v && typeof v.double === "number" && "float" in v && typeof v.float === "number" && "int" in v && typeof v.int === "number";
      }
      """
    }
  }

  "ClassOrObject" in {
    TypescriptGuard.render(decl[ClassOrObject]) shouldBe {
      i"""
      export const isClassOrObject = (v: any): v is ClassOrObject => {
        return typeof v === "object" && v != null && "type" in v && (v.type === "MyClass" ? typeof v === "object" && v != null && "value" in v && typeof v.value === "number" : v.type === "MyObject" ? typeof v === "object" && v != null : false);
      }
      """
    }
  }

  "NestedClassOrObject" in {
    TypescriptGuard.render(decl[NestedClassOrObject]) shouldBe {
      i"""
      export const isNestedClassOrObject = (v: any): v is NestedClassOrObject => {
        return typeof v === "object" && v != null && "type" in v && (v.type === "MyClass" ? typeof v === "object" && v != null && "value" in v && typeof v.value === "number" : v.type === "MyObject" ? typeof v === "object" && v != null : false);
      }
      """
    }
  }

  "Navigation" in {
    TypescriptGuard.render(decl[Navigation]) shouldBe {
      i"""
      export const isNavigation = (v: any): v is Navigation => {
        return typeof v === "object" && v != null && "type" in v && (v.type === "Node" ? typeof v === "object" && v != null && "name" in v && typeof v.name === "string" && "children" in v && Array.isArray(v.children) && v.children.map((i: any) => isNavigation(i)).reduce((a: any, b: any) => a && b) : v.type === "NodeList" ? typeof v === "object" && v != null && "all" in v && Array.isArray(v.all) && v.all.map((i: any) => isNavigation(i)).reduce((a: any, b: any) => a && b) : false);
      }
      """
    }
  }

  "ClassUUID" in {
    TypescriptGuard.render(decl[ClassUUID]) shouldBe {
      i"""
      export const isClassUUID = (v: any): v is ClassUUID => {
        return typeof v === "object" && v != null && "a" in v && isUUID(v.a);
      }
      """
    }
  }

  "ClassDate" in {
    TypescriptGuard.render(decl[ClassDate]) shouldBe {
      i"""
      export const isClassDate = (v: any): v is ClassDate => {
        return typeof v === "object" && v != null && "a" in v && isDate(v.a);
      }
      """
    }
  }

  "Recursive" in {
    TypescriptGuard.render(decl[Recursive]) shouldBe {
      i"""
      export const isRecursive = (v: any): v is Recursive => {
        return typeof v === "object" && v != null && "head" in v && typeof v.head === "number" && (!("tail" in v) || isRecursive(v.tail) || v.tail === null);
      }
      """
    }
  }

  "Recursive2" in {
    TypescriptGuard.render(decl[Recursive2]) shouldBe {
      i"""
      export const isRecursive2 = (v: any): v is Recursive2 => {
        return typeof v === "object" && v != null && "head" in v && typeof v.head === "number" && "tail" in v && Array.isArray(v.tail) && v.tail.map((i: any) => isRecursive2(i)).reduce((a: any, b: any) => a && b);
      }
      """
    }
  }

  "ExternalReferences" in {
    TypescriptGuard.render(decl[ExternalReferences]) shouldBe {
      i"""
      export const isExternalReferences = (v: any): v is ExternalReferences => {
        return typeof v === "object" && v != null && "color" in v && isColor(v.color) && "nav" in v && isNavigation(v.nav);
      }
      """
    }
  }

  "ObjectsOnly" in {
    TypescriptGuard.render(decl[ObjectsOnly]) shouldBe {
      i"""
      export const isObjectsOnly = (v: any): v is ObjectsOnly => {
        return typeof v === "object" && v != null && "type" in v && (v.type === "ObjectOne" ? typeof v === "object" && v != null : v.type === "ObjectTwo" ? typeof v === "object" && v != null : false);
      }
      """
    }
  }

  "Union of Union" in {
    TypescriptGuard.render("A" := Ref("B") | Ref("C") | Ref("D")) shouldBe {
      i"""
      export const isA = (v: any): v is A => {
        return isB(v) || isC(v) || isD(v);
      }
      """
    }
  }

  "Inter of Inter" in {
    TypescriptGuard.render("A" := Ref("B") & Ref("C") & Ref("D")) shouldBe {
      i"""
      export const isA = (v: any): v is A => {
        return isB(v) && isC(v) && isD(v);
      }
      """
    }
  }

  "Generic Decl" in {
    TypescriptGuard.render(
      decl("Pair", "A", "B")(
        struct(
          "a" --> Ref("A"),
          "b" -?> Ref("B")
        )
      )
    ) shouldBe {
      i"""
      export const isPair = <A, B>(isA: (a: any) => boolean, isB: (b: any) => boolean) => (v: any): v is Pair<A, B> => {
        return typeof v === "object" && v != null && "a" in v && isA(v.a) && (!("b" in v) || isB(v.b));
      }
      """
    }
  }

  "Applications of Generics" in {
    TypescriptGuard.render(decl("Cell")(ref("Pair", Str, Intr))) shouldBe {
      i"""
      export const isCell = (v: any): v is Cell => {
        return isPair((a0: any) => typeof a0 === "string", (a1: any) => typeof a1 === "number")(v);
      }
      """
    }

    TypescriptGuard.render(decl("Same", "A")(ref("Pair", ref("A"), ref("A")))) shouldBe {
      i"""
      export const isSame = <A>(isA: (a: any) => boolean) => (v: any): v is Same<A> => {
        return isPair((a0: any) => isA(a0), (a1: any) => isA(a1))(v);
      }
      """
    }

    TypescriptGuard.render(decl("AnyPair")(ref("Pair", Any, Any))) shouldBe {
      i"""
      export const isAnyPair = (v: any): v is AnyPair => {
        return isPair((a0: any) => true, (a1: any) => true)(v);
      }
      """
    }
  }

  "Numeric types" in {
    TypescriptGuard.render(decl[NumericTypes]) shouldBe {
      i"""
      export const isNumericTypes = (v: any): v is NumericTypes => {
        return typeof v === "object" && v != null && "int" in v && typeof v.int === "number" && "long" in v && typeof v.long === "number" && "float" in v && typeof v.float === "number" && "double" in v && typeof v.double === "number" && "bigDecimal" in v && typeof v.bigDecimal === "number";
      }
      """
    }
  }

  "Tuple" in {
    TypescriptGuard.render(decl("Cell")(tuple(Str, Intr))) shouldBe {
      i"""
      export const isCell = (v: any): v is Cell => {
        return Array.isArray(v) && v.length === 2 && typeof v[0] === "string" && typeof v[1] === "number";
      }
      """
    }
  }

  "Empty tuple" in {
    TypescriptGuard.render(decl("Empty")(tuple())) shouldBe {
      i"""
      export const isEmpty = (v: any): v is Empty => {
        return Array.isArray(v) && v.length === 0;
      }
      """
    }
  }
}
