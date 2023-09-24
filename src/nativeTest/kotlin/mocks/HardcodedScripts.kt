package mocks

object HardcodedScripts {
    val testLetAst = """
        {"name":"test_let.rinha","expression":{"kind":"Let","name":{"text":"a","location":{"start":4,"end":5,"filename":"test_let.rinha"}},"value":{"kind":"Int","value":10,"location":{"start":8,"end":10,"filename":"test_let.rinha"}},"next":{"kind":"Let","name":{"text":"b","location":{"start":16,"end":17,"filename":"test_let.rinha"}},"value":{"kind":"Bool","value":false,"location":{"start":20,"end":25,"filename":"test_let.rinha"}},"next":{"kind":"Let","name":{"text":"c","location":{"start":31,"end":32,"filename":"test_let.rinha"}},"value":{"kind":"Bool","value":true,"location":{"start":35,"end":39,"filename":"test_let.rinha"}},"next":{"kind":"Let","name":{"text":"d","location":{"start":45,"end":46,"filename":"test_let.rinha"}},"value":{"kind":"Str","value":"Hello","location":{"start":49,"end":56,"filename":"test_let.rinha"}},"next":{"kind":"Let","name":{"text":"e","location":{"start":62,"end":63,"filename":"test_let.rinha"}},"value":{"kind":"Tuple","first":{"kind":"Int","value":1,"location":{"start":67,"end":68,"filename":"test_let.rinha"}},"second":{"kind":"Int","value":2,"location":{"start":70,"end":71,"filename":"test_let.rinha"}},"location":{"start":66,"end":72,"filename":"test_let.rinha"}},"next":{"kind":"Var","text":"a","location":{"start":74,"end":75,"filename":"test_let.rinha"}},"location":{"start":58,"end":75,"filename":"test_let.rinha"}},"location":{"start":41,"end":75,"filename":"test_let.rinha"}},"location":{"start":27,"end":75,"filename":"test_let.rinha"}},"location":{"start":12,"end":75,"filename":"test_let.rinha"}},"location":{"start":0,"end":75,"filename":"test_let.rinha"}},"location":{"start":0,"end":75,"filename":"test_let.rinha"}}
    """.trimIndent()
    val testLetSource = """
        let a = 10;
        let b = false;
        let c = true;
        let d = "Hello";
        let e = (1, 2);
        a
    """.trimIndent()

    val testMathSource = """
        let t_add = 10 + 20;
        let t_sub = 5 - 10;
        let t_mul = 2 * 3;
        let t_div = 100/5;
        let t_div_zero = 10/0;
        let t_rem = 10 % 2;
        let t_true_1 = 10 == 10;
        let t_true_2 = "a" == "a";
        let t_true_3 = true == true;
        let t_false_1 = 10 != 10;
        let t_false_2 = "a" != "a";
        let t_false_3 = true != false;
        let t_str_add_1 = "a" + "b";
        let t_str_add_2 = "a" + 1;
        let t_str_add_3 = 1 + "a";
        let t_lt = 1 < 2;
        let t_lt_false = 2 < 1;
        let t_lte = 1 <= 1;
        let t_lte_false = 2 <= 1;
        let t_gt = 2 > 1;
        let t_gt_false = 1 > 2;
        let t_gte = 2 >= 2;
        let t_gte_false = 1 >= 2;
        let t_and_false = true && false;
        let t_and_true = true && true;
        let t_or = false || true;
        print("done")
    """.trimIndent()

    val testTupleAst = """
        {"name":"test_tuple.rinha","expression":{"kind":"Let","name":{"text":"target","location":{"start":4,"end":10,"filename":"test_tuple.rinha"}},"value":{"kind":"Tuple","first":{"kind":"Int","value":5,"location":{"start":14,"end":15,"filename":"test_tuple.rinha"}},"second":{"kind":"Int","value":10,"location":{"start":17,"end":19,"filename":"test_tuple.rinha"}},"location":{"start":13,"end":20,"filename":"test_tuple.rinha"}},"next":{"kind":"Let","name":{"text":"primeiro","location":{"start":26,"end":34,"filename":"test_tuple.rinha"}},"value":{"kind":"First","value":{"kind":"Var","text":"target","location":{"start":43,"end":49,"filename":"test_tuple.rinha"}},"location":{"start":37,"end":50,"filename":"test_tuple.rinha"}},"next":{"kind":"Let","name":{"text":"segundo","location":{"start":56,"end":63,"filename":"test_tuple.rinha"}},"value":{"kind":"Second","value":{"kind":"Var","text":"target","location":{"start":73,"end":79,"filename":"test_tuple.rinha"}},"location":{"start":66,"end":80,"filename":"test_tuple.rinha"}},"next":{"kind":"Let","name":{"text":"soma","location":{"start":86,"end":90,"filename":"test_tuple.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Var","text":"primeiro","location":{"start":93,"end":101,"filename":"test_tuple.rinha"}},"op":"Add","rhs":{"kind":"Var","text":"segundo","location":{"start":104,"end":111,"filename":"test_tuple.rinha"}},"location":{"start":93,"end":111,"filename":"test_tuple.rinha"}},"next":{"kind":"Print","value":{"kind":"Binary","lhs":{"kind":"Str","value":"soma = ","location":{"start":119,"end":128,"filename":"test_tuple.rinha"}},"op":"Add","rhs":{"kind":"Var","text":"soma","location":{"start":131,"end":135,"filename":"test_tuple.rinha"}},"location":{"start":119,"end":135,"filename":"test_tuple.rinha"}},"location":{"start":113,"end":136,"filename":"test_tuple.rinha"}},"location":{"start":82,"end":136,"filename":"test_tuple.rinha"}},"location":{"start":52,"end":136,"filename":"test_tuple.rinha"}},"location":{"start":22,"end":136,"filename":"test_tuple.rinha"}},"location":{"start":0,"end":136,"filename":"test_tuple.rinha"}},"location":{"start":0,"end":136,"filename":"test_tuple.rinha"}}
    """.trimIndent()

    val testTupleSource = """
        let target = (5, 10);
        let primeiro = first(target);
        let segundo = second(target);
        let soma = primeiro + segundo;
        print("soma = " + soma)
    """.trimIndent()

    val sumAst = """
        {"name":"sum.rinha","expression":{"kind":"Let","name":{"text":"sum","location":{"start":4,"end":7,"filename":"sum.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n","location":{"start":14,"end":15,"filename":"sum.rinha"}}],"value":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":28,"end":29,"filename":"sum.rinha"}},"op":"Eq","rhs":{"kind":"Int","value":1,"location":{"start":33,"end":34,"filename":"sum.rinha"}},"location":{"start":28,"end":34,"filename":"sum.rinha"}},"then":{"kind":"Var","text":"n","location":{"start":42,"end":43,"filename":"sum.rinha"}},"otherwise":{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":59,"end":60,"filename":"sum.rinha"}},"op":"Add","rhs":{"kind":"Call","callee":{"kind":"Var","text":"sum","location":{"start":63,"end":66,"filename":"sum.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":67,"end":68,"filename":"sum.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":71,"end":72,"filename":"sum.rinha"}},"location":{"start":67,"end":72,"filename":"sum.rinha"}}],"location":{"start":63,"end":73,"filename":"sum.rinha"}},"location":{"start":59,"end":73,"filename":"sum.rinha"}},"location":{"start":24,"end":77,"filename":"sum.rinha"}},"location":{"start":10,"end":79,"filename":"sum.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"sum","location":{"start":89,"end":92,"filename":"sum.rinha"}},"arguments":[{"kind":"Int","value":5,"location":{"start":93,"end":94,"filename":"sum.rinha"}}],"location":{"start":89,"end":95,"filename":"sum.rinha"}},"location":{"start":82,"end":96,"filename":"sum.rinha"}},"location":{"start":0,"end":96,"filename":"sum.rinha"}},"location":{"start":0,"end":96,"filename":"sum.rinha"}}
    """.trimIndent()

    val sumSource = """
        let sum = fn (z) => {
          if (z == 1) {
            z
          } else {
            z + sum(z - 1)
          }
        };

        print (sum(5))
    """.trimIndent()

    val resStringSource = """
        let sumStr = fn (z) => {
          if (z == 1) {
            "" + z
          } else {
            "" + z + sumStr(z - 1)
          }
        };

        print (sumStr(5))
    """.trimIndent()

    val sumCustomSource = """
        let sum = fn (z) => {
          if (z != 1) {
            z + sum(z - 1)
          } else {
            z
          }
        };

        print (sum(1000))
    """.trimIndent()

    val sumMulSource = """
        let sum = fn (z) => {
          if (z != 1) {
            z + (2 * sum(z - 1))
          } else {
            z
          }
        };

        print (sum(10))
    """.trimIndent()

    val printAst = """
        {"name":"print.rinha","expression":{"kind":"Print","value":{"kind":"Str","value":"Hello world","location":{"start":7,"end":20,"filename":"print.rinha"}},"location":{"start":0,"end":21,"filename":"print.rinha"}},"location":{"start":0,"end":21,"filename":"print.rinha"}}
    """.trimIndent()

    val printSource = """
        print ("Hello world")
    """.trimIndent()

    val fibAst = """
        {"name":"fib.rinha","expression":{"kind":"Let","name":{"text":"fib","location":{"start":4,"end":7,"filename":"fib.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n","location":{"start":14,"end":15,"filename":"fib.rinha"}}],"value":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":28,"end":29,"filename":"fib.rinha"}},"op":"Lt","rhs":{"kind":"Int","value":2,"location":{"start":32,"end":33,"filename":"fib.rinha"}},"location":{"start":28,"end":33,"filename":"fib.rinha"}},"then":{"kind":"Var","text":"n","location":{"start":41,"end":42,"filename":"fib.rinha"}},"otherwise":{"kind":"Binary","lhs":{"kind":"Call","callee":{"kind":"Var","text":"fib","location":{"start":58,"end":61,"filename":"fib.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":62,"end":63,"filename":"fib.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":66,"end":67,"filename":"fib.rinha"}},"location":{"start":62,"end":67,"filename":"fib.rinha"}}],"location":{"start":58,"end":68,"filename":"fib.rinha"}},"op":"Add","rhs":{"kind":"Call","callee":{"kind":"Var","text":"fib","location":{"start":71,"end":74,"filename":"fib.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":75,"end":76,"filename":"fib.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":2,"location":{"start":79,"end":80,"filename":"fib.rinha"}},"location":{"start":75,"end":80,"filename":"fib.rinha"}}],"location":{"start":71,"end":81,"filename":"fib.rinha"}},"location":{"start":58,"end":81,"filename":"fib.rinha"}},"location":{"start":24,"end":85,"filename":"fib.rinha"}},"location":{"start":10,"end":87,"filename":"fib.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"fib","location":{"start":96,"end":99,"filename":"fib.rinha"}},"arguments":[{"kind":"Int","value":10,"location":{"start":100,"end":102,"filename":"fib.rinha"}}],"location":{"start":96,"end":103,"filename":"fib.rinha"}},"location":{"start":90,"end":104,"filename":"fib.rinha"}},"location":{"start":0,"end":104,"filename":"fib.rinha"}},"location":{"start":0,"end":104,"filename":"fib.rinha"}}
    """.trimIndent()

    val fibSource = """
        let fib = fn (n) => {
          if (n < 2) {
            n
          } else {
            fib(n - 1) + fib(n - 2)
          }
        };

        print(fib(10))
    """.trimIndent()

    val customRecursiveSource = """
        let rec = fn (z) => {
          let w = 10;
          if (z == 1) {
            z + w
          } else {
            w + z + rec(z - 1)
          }
        };

        print (rec(1000))
    """.trimIndent()

    val fibBreakSource = """
        let fib = fn (n) => {
          if (n < 2) {
            n
          } else {
            fib(n - 1) + fib(n - 2)
          }
        };

        print(fib(1000))
    """.trimIndent()

    val immutabilitySource = """
        let x = 1;
        let foo = fn () => { x };
        let x = 2;
        let foo2 = fn () => { x };
        let result1 = foo();
        let result2 = foo2();
    """.trimIndent()

    val incrementSource = """
        let increment = fn (n) => {
            let add = fn (x) => { n + x }
            add(1)
        }
        print(increment(1))
    """.trimIndent()

    val multiInternalFunctionsSource = """
        let sumX2nX3 = fn (n) => {
            let x2 = fn (x) => { x * 2 }
            let x3 = fn (x) => { x * 3 }
            x2(n) + x3(n)
        }
        print(sumX2nX3(2)) // result = 2*2 + 2*3 => 4 + 6 => 10
    """.trimIndent()

    val commentsSource = """
        let x = 1; // this should be ignored
        /* this should be ignored too */
        // let y = 0
        let /* this should be ignored */ z = 10
        /*
          multi line comment
          */
        let w = 5
    """.trimIndent()

    val functionScopeSource = """
        let f = fn () => {
          let x = 1;
          fn () => {
            x
          }
        };
        let g = f();
        let result = g();
    """.trimIndent()

    val fibtailSource = """
        let fibrec = fn (n, k1, k2) => {
          if (n == 0) {
            k1
          } else {
            if (n == 1) {
              k2
            } else {
              fibrec(n - 1, k2, k1 + k2)
            }
          }
        };
        let fib = fn (n) => {
          fibrec(n, 0, 1)
        };
        print(fib(100000))
    """.trimIndent()

    val combinationAst = """
        {"name":"combination.rinha","expression":{"kind":"Let","name":{"text":"combination","location":{"start":4,"end":15,"filename":"combination.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n","location":{"start":22,"end":23,"filename":"combination.rinha"}},{"text":"k","location":{"start":25,"end":26,"filename":"combination.rinha"}}],"value":{"kind":"Let","name":{"text":"a","location":{"start":41,"end":42,"filename":"combination.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Var","text":"k","location":{"start":45,"end":46,"filename":"combination.rinha"}},"op":"Eq","rhs":{"kind":"Int","value":0,"location":{"start":50,"end":51,"filename":"combination.rinha"}},"location":{"start":45,"end":51,"filename":"combination.rinha"}},"next":{"kind":"Let","name":{"text":"b","location":{"start":61,"end":62,"filename":"combination.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Var","text":"k","location":{"start":65,"end":66,"filename":"combination.rinha"}},"op":"Eq","rhs":{"kind":"Var","text":"n","location":{"start":70,"end":71,"filename":"combination.rinha"}},"location":{"start":65,"end":71,"filename":"combination.rinha"}},"next":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"a","location":{"start":81,"end":82,"filename":"combination.rinha"}},"op":"Or","rhs":{"kind":"Var","text":"b","location":{"start":86,"end":87,"filename":"combination.rinha"}},"location":{"start":81,"end":87,"filename":"combination.rinha"}},"then":{"kind":"Int","value":1,"location":{"start":103,"end":104,"filename":"combination.rinha"}},"otherwise":{"kind":"Binary","lhs":{"kind":"Call","callee":{"kind":"Var","text":"combination","location":{"start":130,"end":141,"filename":"combination.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":142,"end":143,"filename":"combination.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":146,"end":147,"filename":"combination.rinha"}},"location":{"start":142,"end":147,"filename":"combination.rinha"}},{"kind":"Binary","lhs":{"kind":"Var","text":"k","location":{"start":149,"end":150,"filename":"combination.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":153,"end":154,"filename":"combination.rinha"}},"location":{"start":149,"end":154,"filename":"combination.rinha"}}],"location":{"start":130,"end":155,"filename":"combination.rinha"}},"op":"Add","rhs":{"kind":"Call","callee":{"kind":"Var","text":"combination","location":{"start":158,"end":169,"filename":"combination.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":170,"end":171,"filename":"combination.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":174,"end":175,"filename":"combination.rinha"}},"location":{"start":170,"end":175,"filename":"combination.rinha"}},{"kind":"Var","text":"k","location":{"start":177,"end":178,"filename":"combination.rinha"}}],"location":{"start":158,"end":179,"filename":"combination.rinha"}},"location":{"start":130,"end":179,"filename":"combination.rinha"}},"location":{"start":77,"end":185,"filename":"combination.rinha"}},"location":{"start":57,"end":185,"filename":"combination.rinha"}},"location":{"start":37,"end":185,"filename":"combination.rinha"}},"location":{"start":18,"end":187,"filename":"combination.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"combination","location":{"start":196,"end":207,"filename":"combination.rinha"}},"arguments":[{"kind":"Int","value":10,"location":{"start":208,"end":210,"filename":"combination.rinha"}},{"kind":"Int","value":2,"location":{"start":212,"end":213,"filename":"combination.rinha"}}],"location":{"start":196,"end":214,"filename":"combination.rinha"}},"location":{"start":190,"end":215,"filename":"combination.rinha"}},"location":{"start":0,"end":215,"filename":"combination.rinha"}},"location":{"start":0,"end":215,"filename":"combination.rinha"}}
    """.trimIndent()

    val combinationSource = """
        let combination = fn (n, k) => {
            let a = k == 0;
            let b = k == n;
            if (a || b)
            {
                1
            }
            else {
                combination(n - 1, k - 1) + combination(n - 1, k)
            }
        };

        print(combination(10, 2))
    """.trimIndent()

    const val printReturnSouce1 = """
        let _ = print(1); print(2)
    """

    const val printReturnSouce2 = """
        let f = fn (n1, n2, n3) => { n1 };
        f(print(1), print(2), print(3))
    """

    const val printReturnSouce3 = """
        print(print(1) + print(2))
    """

    const val printReturnSouce4 = """
        let tuple = (print(1), print(2)); print(tuple)
    """
}
