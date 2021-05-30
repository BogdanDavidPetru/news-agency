function showAlert() {
    alert("The button was clicked!");
}

/*   Observer Design Pattern -> https://www.youtube.com/watch?v=45TeJEmcqk8
   Author: DevSage (Youtube) -> https://www.youtube.com/DevSage
*/

function Subject()
{
    this.observers = [] // array of observer functions
}

Subject.prototype = {
    subscribe: function(fn)
    {
        this.observers.push(fn)
    },
    unsubscribe: function(fnToRemove)
    {
        this.observers = this.observers.filter( fn => {
            if(fn != fnToRemove)
                return fn
        })
    },
    fire: function()
    {
        this.observers.forEach( fn => {
            fn.call()
        })
    }
}

//const subject = new Subject()

function Observer1()
{
    console.log("Observer 1 Firing!")
}

function Observer2()
{
    console.log("Observer 2 Firing!")
}


function addObs1(){
  //  subject.subscribe(Observer1)
}
function addObs2(){
    //subject.subscribe(Observer2)
}
function fire(){
    //subject.fire()
}
