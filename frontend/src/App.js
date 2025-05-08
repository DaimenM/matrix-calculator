import { useState, useEffect } from 'react';
import './App.css';
import Sidebar from './components/sidebar';

function App() {
  const [matrix1Size,setMatrix1Size] = useState({rows: 3, cols: 3});
  const [matrix2Size, setMatrix2Size] = useState({rows: 3, cols: 3});
  const [matrix1, setMatrix1] = useState(Array.from({length: 3},()=> new Array(3).fill('')));
  const [matrix2, setMatrix2] = useState(Array.from({length: 3},()=> new Array(3).fill('')));
  const [result, setResult] = useState(null);
  const [operation, setOperation] = useState('add'); // Default operation
  const [showMatrix2, setShowMatrix2] = useState(true);
  const [rowCount, setRowCount] = useState(1);
  const [multiplier, setMultiplier] = useState(1);
  const [rowOperation, setRowOperation] = useState('add');
  const [Row1ToManipulate, setRow1ToManipulate] = useState(1)
  const [Row2ToManipulate, setRow2ToManipulate] = useState(1)
  const [exponent, setExponent] = useState(1)
  const [matrixError, setError] = useState("")
  const [showError, setShowError] = useState(false)

  useEffect(() => {
    // Handle matrix2 visibility based on operation
    if (['exponent', 'determinant', 'transpose','RREF','inverse','manipulate'].includes(operation)) {
      setShowMatrix2(false);
    } else if (operation === 'manipulate' && rowCount === 1) {
      setShowMatrix2(false);
    } else {
      setShowMatrix2(true);
    }
  }, [operation, rowCount]);

  const handleSizeChange = (rows,cols, mode) => {
    if(rows===''|| cols==='') return;
    const numRows = Math.min(Math.max(1, rows), 8); // Limit size between 1 and 8
    const numCols = Math.min(Math.max(1, cols), 8);
    if(mode===1) {
      setMatrix1Size({rows:numRows, cols: numCols});
      setMatrix1(Array.from({length: numRows},()=> new Array(numCols).fill('')));
    }
    else if (mode===2) {
      setMatrix2Size({rows:numRows, cols: numCols});
      setMatrix2(Array.from({length: numRows},()=> new Array(numCols).fill('')));

    }
    
    setResult(null);
  };

  const handleMatrix1Change = (row, col, value) => {
    const newMatrix = matrix1.map(r => [...r]);
    newMatrix[row][col] = value;
    setMatrix1(newMatrix);
  };

  const handleMatrix2Change = (row, col, value) => {
    const newMatrix = matrix2.map(r => [...r]);
    newMatrix[row][col] = value;
    setMatrix2(newMatrix);
  };

  async function add(){
    const response = await fetch('/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        matrix1: matrix1,
        matrix1Entries: matrix1.flat().join(','),
        matrix1Rows: matrix1Size.rows,
        matrix1Cols: matrix1Size.cols,
        matrix2: matrix2,
        matrix2Entries: matrix2.flat().join(','), 
        matrix2Rows: matrix2Size.rows,
        matrix2Cols: matrix2Size.cols,
        operation: 'add',
      }),
    })
    if (response.ok) {
      const data = await response.json();
      if(data.error){
        setShowError(true)
        setError(data.error.message)
        setResult(null)
      }
      else {
        setResult(processEntries(data.entries, data.rows, data.columns));
        setShowError(false)
      }
    }
  }
  async function subtract(){
    const response = await fetch('/', {
      method: "POST",
      headers:{
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        matrix1: matrix1,
        matrix1Entries: matrix1.flat().join(","),
        matrix1Rows: matrix1Size.rows,
        matrix1Cols: matrix1Size.cols,
        matrix2: matrix2,
        matrix2Entries: matrix2.flat().join(","),
        matrix2Rows: matrix2Size.rows,
        matrix2Cols:matrix2Size.cols,
        operation:"subtract"
      }),
    })
    if(response.ok){
      const data = await response.json()
      if(data.error){
        setShowError(true)
        setError(data.error.message)
        setResult(null)
      }
      else {
        setResult(processEntries(data.entries, data.rows, data.columns));
        setShowError(false)
      }

    }
  }
  async function multiply(){
    const response = await fetch('/', {
      method: "POST",
      headers:{
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        matrix1: matrix1,
        matrix1Entries: matrix1.flat().join(","),
        matrix1Rows: matrix1Size.rows,
        matrix1Cols: matrix1Size.cols,
        matrix2: matrix2,
        matrix2Entries: matrix2.flat().join(","),
        matrix2Rows: matrix2Size.rows,
        matrix2Cols:matrix2Size.cols,
        operation:"multiply"
      }),
    })
    if(response.ok){
      const data = await response.json()
      if(data.error){
        setShowError(true)
        setError(data.error.message)
        setResult(null)
      }
      else {
        setResult(processEntries(data.entries, data.rows, data.columns));
        setShowError(false)
      }

    }
  }
  async function manipuateRows(){
    if(rowCount===1){
      const response = await fetch("/", {
        method: "POST",
        headers: {
          'Content-Type':'application/json'
        },
        body: JSON.stringify({
          matrix: matrix1,
          matrixEntries: matrix1.flat().join(),
          matrixRows: matrix1Size.rows,
          matrixCols: matrix1Size.cols,
          matrixRow: Row1ToManipulate,
          multiplier: multiplier,
          mode: 1,
          operation: "manipulate"
        }),

      })
      if(response.ok){
        const data = await response.json()
        if(data.error){
          setShowError(true)
          setError(data.error.message)
          setResult(null)
        }
        else {
          setResult(processEntries(data.entries, data.rows, data.columns));
          setShowError(false)
        }
      }
    }
    else if(rowCount===2){
      const response = await fetch("/", {
        method: "POST",
        headers: {
          'Content-Type':'application/json'
        },
        body: JSON.stringify({
          matrix: matrix1,
          matrixEntries: matrix1.flat().join(),
          matrixRows: matrix1Size.rows,
          matrixCols: matrix1Size.cols,
          row1: Row1ToManipulate,
          row2: Row2ToManipulate,
          multiplier: multiplier,
          rowOperation: rowOperation,
          operation: "manipulate",
          mode: 2
        }),

      })
      if(response.ok){
        const data = await response.json()
        if(data.error){
          setShowError(true)
          setError(data.error.message)
          setResult(null)
        }
        else {
          setResult(processEntries(data.entries, data.rows, data.columns));
          setShowError(false)
        }
      }
    }
  }
  async function inverse(){
    const response = await fetch('/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        matrix: matrix1,
        matrixEntries: matrix1.flat().join(','),  // Changed to match controller
        matrixRows: matrix1Size.rows,
        matrixCols: matrix1Size.cols,
        operation: 'inverse',
      }),
})
if (response.ok) {
  const data = await response.json();
  if(data.error){
    setShowError(true)
    setError(data.error.message)
    setResult(null)
  }
  else {
    setResult(processEntries(data.entries, data.rows, data.columns));
    setShowError(false)
  }
}
  }

  async function rref(){
    const response = await fetch('/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        matrix: matrix1,
        matrixEntries: matrix1.flat().join(','),  // Changed to match controller
        matrixRows: matrix1Size.rows,
        matrixCols: matrix1Size.cols,
        operation: 'RREF',
      }),
})
if (response.ok) {
  const data = await response.json();
  if(data.error){
    setShowError(true)
    setError(data.error.message)
    setResult(null)
  }
  else {
    setResult(processEntries(data.entries, data.rows, data.columns));
    setShowError(false)
  }
}
  }
  async function determinant(){
    const response = await fetch('/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        matrix: matrix1,
        matrixEntries: matrix1.flat().join(','),  // Changed to match controller
        matrixRows: matrix1Size.rows,
        matrixCols: matrix1Size.cols,
        operation: 'determinant',
      }),
})
if (response.ok) {
  const data = await response.json();
  if(data.error){
    setShowError(true)
    setError(data.error.message)
    setResult(null)
  }
  else {
    setResult(processEntries(data.entries, data.rows, data.columns));
    setShowError(false)
  }
}
  }

  async function getExponent(){
    const response = await fetch('/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        matrix: matrix1,
        matrixEntries: matrix1.flat().join(),
        matrixRows: matrix1Size.rows,
        matrixCols: matrix1Size.cols,
        exponent: exponent,
        operation: "exponent"
      })
    })
    if(response.ok){
      const data = await response.json()
      if(data.error){
        setShowError(true)
        setError(data.error.message)
        setResult(null)
      }
      else {
        setResult(processEntries(data.entries, data.rows, data.columns));
        setShowError(false)
      }
    }
  }
  async function transpose(){
    const response = await fetch('/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        matrix: matrix1,
        matrixEntries: matrix1.flat().join(','),  // Changed to match controller
        matrixRows: matrix1Size.rows,
        matrixCols: matrix1Size.cols,
        operation: 'transpose',
      }),
})
if (response.ok) {
  const data = await response.json();
  if(data.error){
    setShowError(true)
    setError(data.error.message)
    setResult(null)
  }
  else {
    setResult(processEntries(data.entries, data.rows, data.columns));
    setShowError(false)
  }
}
  }

  const calculateResult = () => {
    switch(operation) {
      case 'add':
        add()
        break;
      case 'subtract':
        subtract()
        break
      case 'multiply':
        multiply()
        break
      case 'RREF':
        rref()
        break;
      case "manipulate":
        manipuateRows()
        break;
      case 'inverse':
        inverse()
        break
      case 'determinant':
        determinant()
        break
      case 'exponent':
        getExponent()
        break;
      case 'transpose':
        transpose()
        break;
      default:
        setResult(null);
    }
  };
  const processEntries = (entries, rows, cols) =>{
    let matrix = Array(Number(rows)).fill().map(() => Array(cols).fill(0));
    for(let i=0; i<rows; i++){
      for(let j=0; j<cols; j++){
        matrix[i][j] = entries[i*cols+j];
      }
    }
    matrix.forEach(row =>{row.forEach(cell => console.log(cell))});
    return matrix;

  }

  return (
    <div className="app-container">
      <Sidebar setOperation={setOperation} currentOperation={operation}/>
      <div className="main-content">
        <div className="header">
          <h1>Matrix Calculator</h1>
        </div>
        {operation === 'manipulate' && (
          <div className="manipulate-controls">
            <div>
              <label>Number of Rows to Manipuate: </label>
              <select onChange={(e) => setRowCount(parseInt(e.target.value))}>
                <option value={1}>One Row</option>
                <option value={2}>Two Rows</option>
              </select>
            </div>
            
            {rowCount === 2 && (
              
              <div>
                <label>Operation: </label>
                <select value={rowOperation} onChange={(e) => setRowOperation(e.target.value)}>
                  <option value="add">Add</option>
                  <option value="subtract">Subtract</option>
                  <option value="multiply">Multiply</option>
                  <option value="switch">Switch Rows</option>
                </select>
              </div>
            )}
          </div>
        )}
        <div className="matrices-container">
          <div className="matrix" id="matrix1Display">
            <h2>Matrix 1</h2>
            {operation === 'manipulate' && rowOperation !== 'switch' && (
              <div className="multiplier-input">
                <label>Multiplier: </label>
                <input
                  type="number"
                  value={multiplier}
                  onChange={(e) => setMultiplier(e.target.value)}
                />
              </div>
            )}
            {operation==='exponent' &&(
              <div className='exponent-input'>
              <label>Exponent: </label>
              <input
                type="number"
                value={exponent}
                min={1}
                onChange={(e) => setExponent(e.target.value)}
              />
            </div>
            )}
            <div className="size-control">
              <input 
                type="number" 
                min="1" 
                max="8" 
                value={matrix1Size.rows} 
                onChange={(e) => {
                  if(e.target.value!=='') 
                    handleSizeChange(parseInt(e.target.value),matrix1Size.cols,1)
                  else 
                    setMatrix1Size({rows:e.target.value,cols:matrix1Size.cols})
                }}
              />
              <p>x</p>
              <input
                type="number" 
                min="1" 
                max="8" 
                value={matrix1Size.cols} 
                onChange={(e) => {
                  if(e.target.value!=='') 
                    handleSizeChange(matrix1Size.rows,parseInt(e.target.value),1)
                  else
                    setMatrix1Size({rows:matrix1Size.rows,cols:e.target.value})

                }}
              />
            </div>
            {matrix1.map((row, i) => (
              <div key={i} className="matrix-row">
                {row.map((cell, j) => (
                  <input
                    key={j}
                    value={cell}
                    onChange={(e) => handleMatrix1Change(i, j, e.target.value)}
                  />
                ))}
              </div>
            ))}
            {operation==='manipulate' &&(
              <div className='rowManipulationInput'>
                <label>Row 1: </label>
                <input
                  type="number"
                  min='1'
                  max={matrix1Size.rows}
                  onChange={(e) => {
                    if(e.target.value>matrix1Size.rows) e.target.value=matrix1Size.rows
                    else if(e.target.value<1&&e.target.value!=='') e.target.value=1
                    setRow1ToManipulate(e.target.value)
                  }}
                />
                {rowCount===2 && (
                <div>
                <label>Row 2: </label>
                <input
                  type="number"
                  min='1'
                  max={matrix1Size.rows}
                  onChange={(e) => {
                    if(e.target.value>matrix1Size.rows) e.target.value=matrix1Size.rows
                    else if(e.target.value<1&&e.target.value!=='') e.target.value=1
                    setRow2ToManipulate(e.target.value)
                  }}
                />
                </div>
                )}

              </div>
            )}
          </div>

          {showMatrix2 && (
            <div className="matrix" id="matrix2Display">
              <h2>Matrix 2</h2>
              <div className="size-control">
                <input 
                  type="number" 
                  min="1" 
                  max="8" 
                  value={matrix2Size.rows} 
                  onChange={(e) => {
                    if(e.target.value!=='') 
                      handleSizeChange(parseInt(e.target.value),matrix2Size.cols,2)
                    else 
                      setMatrix2Size({rows:e.target.value,cols:matrix2Size.cols})
                  }}
                />
                <p>x</p>
                <input
                  type="number" 
                  min="1" 
                  max="8" 
                  value={matrix2Size.cols} 
                  onChange={(e) => {
                    if(e.target.value!=='') 
                      handleSizeChange(matrix2Size.rows,parseInt(e.target.value),2)
                    else
                      setMatrix2Size({rows:matrix2Size.rows,cols:e.target.value})

                  }}
                />
              </div>
              {matrix2.map((row, i) => (
                <div key={i} className="matrix-row">
                  {row.map((cell, j) => (
                    <input
                      key={j}
                      value={cell}
                      onChange={(e) => handleMatrix2Change(i, j, e.target.value)}
                    />
                  ))}
                </div>
              ))}
            </div>
          )}
        </div>
        <div className="operations">
        <button onClick={() =>{
              setMatrix1(Array.from({length: matrix1Size.rows},()=> new Array(matrix1Size.cols).fill('')));
              setMatrix2(Array.from({length: matrix2Size.rows},()=> new Array(matrix2Size.cols).fill('')));
              setResult(null);
              setShowError(false)
            }}
            >Clear</button>
            <button onClick={calculateResult}>Calculate</button>
          </div>

        {result && (
          <div className="matrix result">
            <h2>Result</h2>
            {result.map((row, i) => (
              <div key={i} className="matrix-row">
                {row.map((cell, j) => (
                  <div key={j} className="result-cell">{cell}</div>
                ))}
              </div>
            ))}
          </div>
        )}
        { showError&&(
          <div className='error-message'>
            <h2 id='error'>Error: {matrixError}</h2>
            </div>
        )}
      </div>
    </div>
  );
}
export default App;
