package ast

object AstMocks {
    const val LET = """
        {"name":"files/ast/let.rinha","expression":{"kind":"Let","name":{"text":"x","location":{"start":4,"end":5,"filename":"files/ast/let.rinha"}},"value":{"kind":"Int","value":10,"location":{"start":8,"end":10,"filename":"files/ast/let.rinha"}},"next":{"kind":"Print","value":{"kind":"Var","text":"x","location":{"start":18,"end":19,"filename":"files/ast/let.rinha"}},"location":{"start":12,"end":20,"filename":"files/ast/let.rinha"}},"location":{"start":0,"end":20,"filename":"files/ast/let.rinha"}},"location":{"start":0,"end":20,"filename":"files/ast/let.rinha"}}
    """
}
