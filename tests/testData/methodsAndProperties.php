<?php
class A {<fold text='...'>
    public $b;
    public function aa() {}
</fold>}

class B {<fold text='...'>
    protected $a;
    public function aa() {<fold text='...'>
        <fold text=''>$this-></fold>a;
        <fold text=''>$this-></fold>bb();

        $a = new A();
        $a->aa();
        $a->b;
    </fold>}
    public function bb() {}
</fold>}