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

    val testMathSourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"t_add","location":{"start":4,"end":9,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":10,"location":{"start":12,"end":14,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Int","value":20,"location":{"start":17,"end":19,"filename":"test.rinha"}},"location":{"start":12,"end":19,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_sub","location":{"start":25,"end":30,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":5,"location":{"start":33,"end":34,"filename":"test.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":10,"location":{"start":37,"end":39,"filename":"test.rinha"}},"location":{"start":33,"end":39,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_mul","location":{"start":45,"end":50,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":2,"location":{"start":53,"end":54,"filename":"test.rinha"}},"op":"Mul","rhs":{"kind":"Int","value":3,"location":{"start":57,"end":58,"filename":"test.rinha"}},"location":{"start":53,"end":58,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_div","location":{"start":64,"end":69,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":100,"location":{"start":72,"end":75,"filename":"test.rinha"}},"op":"Div","rhs":{"kind":"Int","value":5,"location":{"start":76,"end":77,"filename":"test.rinha"}},"location":{"start":72,"end":77,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_div_zero","location":{"start":83,"end":93,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":10,"location":{"start":96,"end":98,"filename":"test.rinha"}},"op":"Div","rhs":{"kind":"Int","value":0,"location":{"start":99,"end":100,"filename":"test.rinha"}},"location":{"start":96,"end":100,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_rem","location":{"start":106,"end":111,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":10,"location":{"start":114,"end":116,"filename":"test.rinha"}},"op":"Rem","rhs":{"kind":"Int","value":2,"location":{"start":119,"end":120,"filename":"test.rinha"}},"location":{"start":114,"end":120,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_true_1","location":{"start":126,"end":134,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":10,"location":{"start":137,"end":139,"filename":"test.rinha"}},"op":"Eq","rhs":{"kind":"Int","value":10,"location":{"start":143,"end":145,"filename":"test.rinha"}},"location":{"start":137,"end":145,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_true_2","location":{"start":151,"end":159,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Str","value":"a","location":{"start":162,"end":165,"filename":"test.rinha"}},"op":"Eq","rhs":{"kind":"Str","value":"a","location":{"start":169,"end":172,"filename":"test.rinha"}},"location":{"start":162,"end":172,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_true_3","location":{"start":178,"end":186,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Bool","value":true,"location":{"start":189,"end":193,"filename":"test.rinha"}},"op":"Eq","rhs":{"kind":"Bool","value":true,"location":{"start":197,"end":201,"filename":"test.rinha"}},"location":{"start":189,"end":201,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_false_1","location":{"start":207,"end":216,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":10,"location":{"start":219,"end":221,"filename":"test.rinha"}},"op":"Neq","rhs":{"kind":"Int","value":10,"location":{"start":225,"end":227,"filename":"test.rinha"}},"location":{"start":219,"end":227,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_false_2","location":{"start":233,"end":242,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Str","value":"a","location":{"start":245,"end":248,"filename":"test.rinha"}},"op":"Neq","rhs":{"kind":"Str","value":"a","location":{"start":252,"end":255,"filename":"test.rinha"}},"location":{"start":245,"end":255,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_false_3","location":{"start":261,"end":270,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Bool","value":true,"location":{"start":273,"end":277,"filename":"test.rinha"}},"op":"Neq","rhs":{"kind":"Bool","value":false,"location":{"start":281,"end":286,"filename":"test.rinha"}},"location":{"start":273,"end":286,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_str_add_1","location":{"start":292,"end":303,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Str","value":"a","location":{"start":306,"end":309,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Str","value":"b","location":{"start":312,"end":315,"filename":"test.rinha"}},"location":{"start":306,"end":315,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_str_add_2","location":{"start":321,"end":332,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Str","value":"a","location":{"start":335,"end":338,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Int","value":1,"location":{"start":341,"end":342,"filename":"test.rinha"}},"location":{"start":335,"end":342,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_str_add_3","location":{"start":348,"end":359,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":1,"location":{"start":362,"end":363,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Str","value":"a","location":{"start":366,"end":369,"filename":"test.rinha"}},"location":{"start":362,"end":369,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_lt","location":{"start":375,"end":379,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":1,"location":{"start":382,"end":383,"filename":"test.rinha"}},"op":"Lt","rhs":{"kind":"Int","value":2,"location":{"start":386,"end":387,"filename":"test.rinha"}},"location":{"start":382,"end":387,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_lt_false","location":{"start":393,"end":403,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":2,"location":{"start":406,"end":407,"filename":"test.rinha"}},"op":"Lt","rhs":{"kind":"Int","value":1,"location":{"start":410,"end":411,"filename":"test.rinha"}},"location":{"start":406,"end":411,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_lte","location":{"start":417,"end":422,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":1,"location":{"start":425,"end":426,"filename":"test.rinha"}},"op":"Lte","rhs":{"kind":"Int","value":1,"location":{"start":430,"end":431,"filename":"test.rinha"}},"location":{"start":425,"end":431,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_lte_false","location":{"start":437,"end":448,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":2,"location":{"start":451,"end":452,"filename":"test.rinha"}},"op":"Lte","rhs":{"kind":"Int","value":1,"location":{"start":456,"end":457,"filename":"test.rinha"}},"location":{"start":451,"end":457,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_gt","location":{"start":463,"end":467,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":2,"location":{"start":470,"end":471,"filename":"test.rinha"}},"op":"Gt","rhs":{"kind":"Int","value":1,"location":{"start":474,"end":475,"filename":"test.rinha"}},"location":{"start":470,"end":475,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_gt_false","location":{"start":481,"end":491,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":1,"location":{"start":494,"end":495,"filename":"test.rinha"}},"op":"Gt","rhs":{"kind":"Int","value":2,"location":{"start":498,"end":499,"filename":"test.rinha"}},"location":{"start":494,"end":499,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_gte","location":{"start":505,"end":510,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":2,"location":{"start":513,"end":514,"filename":"test.rinha"}},"op":"Gte","rhs":{"kind":"Int","value":2,"location":{"start":518,"end":519,"filename":"test.rinha"}},"location":{"start":513,"end":519,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_gte_false","location":{"start":525,"end":536,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Int","value":1,"location":{"start":539,"end":540,"filename":"test.rinha"}},"op":"Gte","rhs":{"kind":"Int","value":2,"location":{"start":544,"end":545,"filename":"test.rinha"}},"location":{"start":539,"end":545,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_and_false","location":{"start":551,"end":562,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Bool","value":true,"location":{"start":565,"end":569,"filename":"test.rinha"}},"op":"And","rhs":{"kind":"Bool","value":false,"location":{"start":573,"end":578,"filename":"test.rinha"}},"location":{"start":565,"end":578,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_and_true","location":{"start":584,"end":594,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Bool","value":true,"location":{"start":597,"end":601,"filename":"test.rinha"}},"op":"And","rhs":{"kind":"Bool","value":true,"location":{"start":605,"end":609,"filename":"test.rinha"}},"location":{"start":597,"end":609,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"t_or","location":{"start":615,"end":619,"filename":"test.rinha"}},"value":{"kind":"Binary","lhs":{"kind":"Bool","value":false,"location":{"start":622,"end":627,"filename":"test.rinha"}},"op":"Or","rhs":{"kind":"Bool","value":true,"location":{"start":631,"end":635,"filename":"test.rinha"}},"location":{"start":622,"end":635,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Str","value":"done","location":{"start":643,"end":649,"filename":"test.rinha"}},"location":{"start":637,"end":650,"filename":"test.rinha"}},"location":{"start":611,"end":650,"filename":"test.rinha"}},"location":{"start":580,"end":650,"filename":"test.rinha"}},"location":{"start":547,"end":650,"filename":"test.rinha"}},"location":{"start":521,"end":650,"filename":"test.rinha"}},"location":{"start":501,"end":650,"filename":"test.rinha"}},"location":{"start":477,"end":650,"filename":"test.rinha"}},"location":{"start":459,"end":650,"filename":"test.rinha"}},"location":{"start":433,"end":650,"filename":"test.rinha"}},"location":{"start":413,"end":650,"filename":"test.rinha"}},"location":{"start":389,"end":650,"filename":"test.rinha"}},"location":{"start":371,"end":650,"filename":"test.rinha"}},"location":{"start":344,"end":650,"filename":"test.rinha"}},"location":{"start":317,"end":650,"filename":"test.rinha"}},"location":{"start":288,"end":650,"filename":"test.rinha"}},"location":{"start":257,"end":650,"filename":"test.rinha"}},"location":{"start":229,"end":650,"filename":"test.rinha"}},"location":{"start":203,"end":650,"filename":"test.rinha"}},"location":{"start":174,"end":650,"filename":"test.rinha"}},"location":{"start":147,"end":650,"filename":"test.rinha"}},"location":{"start":122,"end":650,"filename":"test.rinha"}},"location":{"start":102,"end":650,"filename":"test.rinha"}},"location":{"start":79,"end":650,"filename":"test.rinha"}},"location":{"start":60,"end":650,"filename":"test.rinha"}},"location":{"start":41,"end":650,"filename":"test.rinha"}},"location":{"start":21,"end":650,"filename":"test.rinha"}},"location":{"start":0,"end":650,"filename":"test.rinha"}},"location":{"start":0,"end":650,"filename":"test.rinha"}}
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

    val resStringSourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"sumStr","location":{"start":12,"end":18,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"z","location":{"start":25,"end":26,"filename":"test.rinha"}}],"value":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":47,"end":48,"filename":"test.rinha"}},"op":"Eq","rhs":{"kind":"Int","value":1,"location":{"start":52,"end":53,"filename":"test.rinha"}},"location":{"start":47,"end":53,"filename":"test.rinha"}},"then":{"kind":"Binary","lhs":{"kind":"Str","value":"","location":{"start":69,"end":71,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Var","text":"z","location":{"start":74,"end":75,"filename":"test.rinha"}},"location":{"start":69,"end":75,"filename":"test.rinha"}},"otherwise":{"kind":"Binary","lhs":{"kind":"Str","value":"","location":{"start":107,"end":109,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":112,"end":113,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Call","callee":{"kind":"Var","text":"sumStr","location":{"start":116,"end":122,"filename":"test.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":123,"end":124,"filename":"test.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":127,"end":128,"filename":"test.rinha"}},"location":{"start":123,"end":128,"filename":"test.rinha"}}],"location":{"start":116,"end":129,"filename":"test.rinha"}},"location":{"start":112,"end":129,"filename":"test.rinha"}},"location":{"start":107,"end":129,"filename":"test.rinha"}},"location":{"start":43,"end":141,"filename":"test.rinha"}},"location":{"start":21,"end":151,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"sumStr","location":{"start":169,"end":175,"filename":"test.rinha"}},"arguments":[{"kind":"Int","value":5,"location":{"start":176,"end":177,"filename":"test.rinha"}}],"location":{"start":169,"end":178,"filename":"test.rinha"}},"location":{"start":162,"end":179,"filename":"test.rinha"}},"location":{"start":8,"end":179,"filename":"test.rinha"}},"location":{"start":8,"end":179,"filename":"test.rinha"}}
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

    val sumCustomSourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"sum","location":{"start":4,"end":7,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"z","location":{"start":14,"end":15,"filename":"test.rinha"}}],"value":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":34,"end":35,"filename":"test.rinha"}},"op":"Neq","rhs":{"kind":"Int","value":1,"location":{"start":39,"end":40,"filename":"test.rinha"}},"location":{"start":34,"end":40,"filename":"test.rinha"}},"then":{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":52,"end":53,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Call","callee":{"kind":"Var","text":"sum","location":{"start":56,"end":59,"filename":"test.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":60,"end":61,"filename":"test.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":64,"end":65,"filename":"test.rinha"}},"location":{"start":60,"end":65,"filename":"test.rinha"}}],"location":{"start":56,"end":66,"filename":"test.rinha"}},"location":{"start":52,"end":66,"filename":"test.rinha"}},"otherwise":{"kind":"Var","text":"z","location":{"start":92,"end":93,"filename":"test.rinha"}},"location":{"start":30,"end":103,"filename":"test.rinha"}},"location":{"start":10,"end":109,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"sum","location":{"start":119,"end":122,"filename":"test.rinha"}},"arguments":[{"kind":"Int","value":1000,"location":{"start":123,"end":127,"filename":"test.rinha"}}],"location":{"start":119,"end":128,"filename":"test.rinha"}},"location":{"start":112,"end":129,"filename":"test.rinha"}},"location":{"start":0,"end":129,"filename":"test.rinha"}},"location":{"start":0,"end":129,"filename":"test.rinha"}}
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

    val sumMulSourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"sum","location":{"start":4,"end":7,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"z","location":{"start":14,"end":15,"filename":"test.rinha"}}],"value":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":30,"end":31,"filename":"test.rinha"}},"op":"Neq","rhs":{"kind":"Int","value":1,"location":{"start":35,"end":36,"filename":"test.rinha"}},"location":{"start":30,"end":36,"filename":"test.rinha"}},"then":{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":44,"end":45,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Binary","lhs":{"kind":"Int","value":2,"location":{"start":49,"end":50,"filename":"test.rinha"}},"op":"Mul","rhs":{"kind":"Call","callee":{"kind":"Var","text":"sum","location":{"start":53,"end":56,"filename":"test.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"z","location":{"start":57,"end":58,"filename":"test.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":61,"end":62,"filename":"test.rinha"}},"location":{"start":57,"end":62,"filename":"test.rinha"}}],"location":{"start":53,"end":63,"filename":"test.rinha"}},"location":{"start":49,"end":63,"filename":"test.rinha"}},"location":{"start":44,"end":64,"filename":"test.rinha"}},"otherwise":{"kind":"Var","text":"z","location":{"start":82,"end":83,"filename":"test.rinha"}},"location":{"start":26,"end":89,"filename":"test.rinha"}},"location":{"start":10,"end":91,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"sum","location":{"start":101,"end":104,"filename":"test.rinha"}},"arguments":[{"kind":"Int","value":10,"location":{"start":105,"end":107,"filename":"test.rinha"}}],"location":{"start":101,"end":108,"filename":"test.rinha"}},"location":{"start":94,"end":109,"filename":"test.rinha"}},"location":{"start":0,"end":109,"filename":"test.rinha"}},"location":{"start":0,"end":109,"filename":"test.rinha"}}
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

    val immutabilitySourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"x","location":{"start":4,"end":5,"filename":"test.rinha"}},"value":{"kind":"Int","value":1,"location":{"start":8,"end":9,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"foo","location":{"start":15,"end":18,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[],"value":{"kind":"Var","text":"x","location":{"start":32,"end":33,"filename":"test.rinha"}},"location":{"start":21,"end":35,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"x","location":{"start":41,"end":42,"filename":"test.rinha"}},"value":{"kind":"Int","value":2,"location":{"start":45,"end":46,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"foo2","location":{"start":52,"end":56,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[],"value":{"kind":"Var","text":"x","location":{"start":70,"end":71,"filename":"test.rinha"}},"location":{"start":59,"end":73,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"result1","location":{"start":79,"end":86,"filename":"test.rinha"}},"value":{"kind":"Call","callee":{"kind":"Var","text":"foo","location":{"start":89,"end":92,"filename":"test.rinha"}},"arguments":[],"location":{"start":89,"end":94,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"result2","location":{"start":100,"end":107,"filename":"test.rinha"}},"value":{"kind":"Call","callee":{"kind":"Var","text":"foo2","location":{"start":110,"end":114,"filename":"test.rinha"}},"arguments":[],"location":{"start":110,"end":116,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Str","value":"done","location":{"start":124,"end":130,"filename":"test.rinha"}},"location":{"start":118,"end":131,"filename":"test.rinha"}},"location":{"start":96,"end":131,"filename":"test.rinha"}},"location":{"start":75,"end":131,"filename":"test.rinha"}},"location":{"start":48,"end":131,"filename":"test.rinha"}},"location":{"start":37,"end":131,"filename":"test.rinha"}},"location":{"start":11,"end":131,"filename":"test.rinha"}},"location":{"start":0,"end":131,"filename":"test.rinha"}},"location":{"start":0,"end":131,"filename":"test.rinha"}}
    """.trimIndent()

    val immutabilitySource = """
        let x = 1;
        let foo = fn () => { x };
        let x = 2;
        let foo2 = fn () => { x };
        let result1 = foo();
        let result2 = foo2()
    """.trimIndent()

    val incrementSourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"increment","location":{"start":12,"end":21,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n","location":{"start":28,"end":29,"filename":"test.rinha"}}],"value":{"kind":"Let","name":{"text":"add","location":{"start":52,"end":55,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"x","location":{"start":62,"end":63,"filename":"test.rinha"}}],"value":{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":70,"end":71,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Var","text":"x","location":{"start":74,"end":75,"filename":"test.rinha"}},"location":{"start":70,"end":75,"filename":"test.rinha"}},"location":{"start":58,"end":77,"filename":"test.rinha"}},"next":{"kind":"Call","callee":{"kind":"Var","text":"add","location":{"start":91,"end":94,"filename":"test.rinha"}},"arguments":[{"kind":"Int","value":1,"location":{"start":95,"end":96,"filename":"test.rinha"}}],"location":{"start":91,"end":97,"filename":"test.rinha"}},"location":{"start":48,"end":97,"filename":"test.rinha"}},"location":{"start":24,"end":107,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"increment","location":{"start":123,"end":132,"filename":"test.rinha"}},"arguments":[{"kind":"Int","value":1,"location":{"start":133,"end":134,"filename":"test.rinha"}}],"location":{"start":123,"end":135,"filename":"test.rinha"}},"location":{"start":117,"end":136,"filename":"test.rinha"}},"location":{"start":8,"end":136,"filename":"test.rinha"}},"location":{"start":8,"end":136,"filename":"test.rinha"}}
    """.trimIndent()

    val incrementSource = """
        let increment = fn (n) => {
            let add = fn (x) => { n + x }
            add(1)
        }
        print(increment(1))
    """.trimIndent()

    val multiInternalFunctionsSourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"sumX2nX3","location":{"start":12,"end":20,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n","location":{"start":27,"end":28,"filename":"test.rinha"}}],"value":{"kind":"Let","name":{"text":"x2","location":{"start":51,"end":53,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"x","location":{"start":60,"end":61,"filename":"test.rinha"}}],"value":{"kind":"Binary","lhs":{"kind":"Var","text":"x","location":{"start":68,"end":69,"filename":"test.rinha"}},"op":"Mul","rhs":{"kind":"Int","value":2,"location":{"start":72,"end":73,"filename":"test.rinha"}},"location":{"start":68,"end":73,"filename":"test.rinha"}},"location":{"start":56,"end":75,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"x3","location":{"start":93,"end":95,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"x","location":{"start":102,"end":103,"filename":"test.rinha"}}],"value":{"kind":"Binary","lhs":{"kind":"Var","text":"x","location":{"start":110,"end":111,"filename":"test.rinha"}},"op":"Mul","rhs":{"kind":"Int","value":3,"location":{"start":114,"end":115,"filename":"test.rinha"}},"location":{"start":110,"end":115,"filename":"test.rinha"}},"location":{"start":98,"end":117,"filename":"test.rinha"}},"next":{"kind":"Binary","lhs":{"kind":"Call","callee":{"kind":"Var","text":"x2","location":{"start":131,"end":133,"filename":"test.rinha"}},"arguments":[{"kind":"Var","text":"n","location":{"start":134,"end":135,"filename":"test.rinha"}}],"location":{"start":131,"end":136,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Call","callee":{"kind":"Var","text":"x3","location":{"start":139,"end":141,"filename":"test.rinha"}},"arguments":[{"kind":"Var","text":"n","location":{"start":142,"end":143,"filename":"test.rinha"}}],"location":{"start":139,"end":144,"filename":"test.rinha"}},"location":{"start":131,"end":144,"filename":"test.rinha"}},"location":{"start":89,"end":144,"filename":"test.rinha"}},"location":{"start":47,"end":144,"filename":"test.rinha"}},"location":{"start":23,"end":154,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"sumX2nX3","location":{"start":170,"end":178,"filename":"test.rinha"}},"arguments":[{"kind":"Int","value":2,"location":{"start":179,"end":180,"filename":"test.rinha"}}],"location":{"start":170,"end":181,"filename":"test.rinha"}},"location":{"start":164,"end":182,"filename":"test.rinha"}},"location":{"start":8,"end":182,"filename":"test.rinha"}},"location":{"start":8,"end":182,"filename":"test.rinha"}}
    """.trimIndent()

    val multiInternalFunctionsSource = """
        let sumX2nX3 = fn (n) => {
            let x2 = fn (x) => { x * 2 };
            let x3 = fn (x) => { x * 3 };
            x2(n) + x3(n)
        };
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

    val fibtailSourceAst = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"fibrec","location":{"start":4,"end":10,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n","location":{"start":17,"end":18,"filename":"test.rinha"}},{"text":"k1","location":{"start":20,"end":22,"filename":"test.rinha"}},{"text":"k2","location":{"start":24,"end":26,"filename":"test.rinha"}}],"value":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":47,"end":48,"filename":"test.rinha"}},"op":"Eq","rhs":{"kind":"Int","value":0,"location":{"start":52,"end":53,"filename":"test.rinha"}},"location":{"start":47,"end":53,"filename":"test.rinha"}},"then":{"kind":"Var","text":"k1","location":{"start":69,"end":71,"filename":"test.rinha"}},"otherwise":{"kind":"If","condition":{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":107,"end":108,"filename":"test.rinha"}},"op":"Eq","rhs":{"kind":"Int","value":1,"location":{"start":112,"end":113,"filename":"test.rinha"}},"location":{"start":107,"end":113,"filename":"test.rinha"}},"then":{"kind":"Var","text":"k2","location":{"start":131,"end":133,"filename":"test.rinha"}},"otherwise":{"kind":"Call","callee":{"kind":"Var","text":"fibrec","location":{"start":169,"end":175,"filename":"test.rinha"}},"arguments":[{"kind":"Binary","lhs":{"kind":"Var","text":"n","location":{"start":176,"end":177,"filename":"test.rinha"}},"op":"Sub","rhs":{"kind":"Int","value":1,"location":{"start":180,"end":181,"filename":"test.rinha"}},"location":{"start":176,"end":181,"filename":"test.rinha"}},{"kind":"Var","text":"k2","location":{"start":183,"end":185,"filename":"test.rinha"}},{"kind":"Binary","lhs":{"kind":"Var","text":"k1","location":{"start":187,"end":189,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Var","text":"k2","location":{"start":192,"end":194,"filename":"test.rinha"}},"location":{"start":187,"end":194,"filename":"test.rinha"}}],"location":{"start":169,"end":195,"filename":"test.rinha"}},"location":{"start":103,"end":209,"filename":"test.rinha"}},"location":{"start":43,"end":221,"filename":"test.rinha"}},"location":{"start":13,"end":231,"filename":"test.rinha"}},"next":{"kind":"Let","name":{"text":"fib","location":{"start":245,"end":248,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n","location":{"start":255,"end":256,"filename":"test.rinha"}}],"value":{"kind":"Call","callee":{"kind":"Var","text":"fibrec","location":{"start":273,"end":279,"filename":"test.rinha"}},"arguments":[{"kind":"Var","text":"n","location":{"start":280,"end":281,"filename":"test.rinha"}},{"kind":"Int","value":0,"location":{"start":283,"end":284,"filename":"test.rinha"}},{"kind":"Int","value":1,"location":{"start":286,"end":287,"filename":"test.rinha"}}],"location":{"start":273,"end":288,"filename":"test.rinha"}},"location":{"start":251,"end":298,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Call","callee":{"kind":"Var","text":"fib","location":{"start":314,"end":317,"filename":"test.rinha"}},"arguments":[{"kind":"Int","value":100000,"location":{"start":318,"end":324,"filename":"test.rinha"}}],"location":{"start":314,"end":325,"filename":"test.rinha"}},"location":{"start":308,"end":326,"filename":"test.rinha"}},"location":{"start":241,"end":326,"filename":"test.rinha"}},"location":{"start":0,"end":326,"filename":"test.rinha"}},"location":{"start":0,"end":326,"filename":"test.rinha"}}
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

    val printReturnSource1Ast = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"_","location":{"start":4,"end":5,"filename":"test.rinha"}},"value":{"kind":"Print","value":{"kind":"Int","value":1,"location":{"start":14,"end":15,"filename":"test.rinha"}},"location":{"start":8,"end":16,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Int","value":2,"location":{"start":24,"end":25,"filename":"test.rinha"}},"location":{"start":18,"end":26,"filename":"test.rinha"}},"location":{"start":0,"end":26,"filename":"test.rinha"}},"location":{"start":0,"end":26,"filename":"test.rinha"}}
    """.trimIndent()

    const val printReturnSouce1 = """
        let _ = print(1); print(2)
    """

    val printReturnSource2Ast = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"f","location":{"start":4,"end":5,"filename":"test.rinha"}},"value":{"kind":"Function","parameters":[{"text":"n1","location":{"start":12,"end":14,"filename":"test.rinha"}},{"text":"n2","location":{"start":16,"end":18,"filename":"test.rinha"}},{"text":"n3","location":{"start":20,"end":22,"filename":"test.rinha"}}],"value":{"kind":"Var","text":"n1","location":{"start":29,"end":31,"filename":"test.rinha"}},"location":{"start":8,"end":33,"filename":"test.rinha"}},"next":{"kind":"Call","callee":{"kind":"Var","text":"f","location":{"start":35,"end":36,"filename":"test.rinha"}},"arguments":[{"kind":"Print","value":{"kind":"Int","value":1,"location":{"start":43,"end":44,"filename":"test.rinha"}},"location":{"start":37,"end":45,"filename":"test.rinha"}},{"kind":"Print","value":{"kind":"Int","value":2,"location":{"start":53,"end":54,"filename":"test.rinha"}},"location":{"start":47,"end":55,"filename":"test.rinha"}},{"kind":"Print","value":{"kind":"Int","value":3,"location":{"start":63,"end":64,"filename":"test.rinha"}},"location":{"start":57,"end":65,"filename":"test.rinha"}}],"location":{"start":35,"end":66,"filename":"test.rinha"}},"location":{"start":0,"end":66,"filename":"test.rinha"}},"location":{"start":0,"end":66,"filename":"test.rinha"}}
    """.trimIndent()

    const val printReturnSouce2 = """
        let f = fn (n1, n2, n3) => { n1 };
        f(print(1), print(2), print(3))
    """

    val printReturnSource3Ast = """
        {"name":"test.rinha","expression":{"kind":"Print","value":{"kind":"Binary","lhs":{"kind":"Print","value":{"kind":"Int","value":1,"location":{"start":12,"end":13,"filename":"test.rinha"}},"location":{"start":6,"end":14,"filename":"test.rinha"}},"op":"Add","rhs":{"kind":"Print","value":{"kind":"Int","value":2,"location":{"start":23,"end":24,"filename":"test.rinha"}},"location":{"start":17,"end":25,"filename":"test.rinha"}},"location":{"start":6,"end":25,"filename":"test.rinha"}},"location":{"start":0,"end":26,"filename":"test.rinha"}},"location":{"start":0,"end":26,"filename":"test.rinha"}}
    """.trimIndent()

    const val printReturnSource3 = """
        print(print(1) + print(2))
    """

    val printReturnSource4Ast = """
        {"name":"test.rinha","expression":{"kind":"Let","name":{"text":"tuple","location":{"start":4,"end":9,"filename":"test.rinha"}},"value":{"kind":"Tuple","first":{"kind":"Print","value":{"kind":"Int","value":1,"location":{"start":19,"end":20,"filename":"test.rinha"}},"location":{"start":13,"end":21,"filename":"test.rinha"}},"second":{"kind":"Print","value":{"kind":"Int","value":2,"location":{"start":29,"end":30,"filename":"test.rinha"}},"location":{"start":23,"end":31,"filename":"test.rinha"}},"location":{"start":12,"end":32,"filename":"test.rinha"}},"next":{"kind":"Print","value":{"kind":"Var","text":"tuple","location":{"start":40,"end":45,"filename":"test.rinha"}},"location":{"start":34,"end":46,"filename":"test.rinha"}},"location":{"start":0,"end":46,"filename":"test.rinha"}},"location":{"start":0,"end":46,"filename":"test.rinha"}}
    """.trimIndent()

    const val printReturnSource4 = """
        let tuple = (print(1), print(2)); print(tuple)
    """
}
