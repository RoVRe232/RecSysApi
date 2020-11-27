
console.log("hello world!");

const userAction = async () => {
    const response = await fetch('http://localhost:8080/search?keywords=hombre&usertoken=q2za-rtx5-4asd-rrrw');
    const myJson = await response.json(); //extract JSON from the http response
    // do something with myJson
    console.log(myJson);
  }