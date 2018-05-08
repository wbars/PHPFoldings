<?php
namespace A {
    class B {<fold text='...'>
        public static function b(){}
    </fold>}
}

namespace B {
    $b = new <fold text='...'>\A</fold>\B();
    <fold text='...'>\A</fold>\B::b();
    function b(<fold text='...'>\A</fold>\B $b) {<fold text='...'>

    </fold>}
}

namespace C {
    use A\B;
    $d = new \B();
}

namespace D {
    /**<fold text=' @method \A\B b ...'>
     * @method \A\B b
    </fold>*/
    class A {}
}