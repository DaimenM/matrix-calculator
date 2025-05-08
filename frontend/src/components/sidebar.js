import React, { useState } from 'react';
import './sidebar.css';

const Sidebar = ({ setOperation, currentOperation }) => {
  const [isCollapsed, setIsCollapsed] = useState(false);

  const operations = [
    { id: 'add', name: 'Addition' },
    { id: 'subtract', name: 'Subtraction' },
    { id: 'multiply', name: 'Multiplication' },
    {id: 'manipulate', name: 'Manipulate rows'},
    {id: 'RREF', name: 'RREF'},
    { id: 'inverse', name: 'Inverse' },
    { id: 'determinant', name: 'Determinant' },
    {id: 'exponent', name: 'Exponent'},
    { id: 'transpose', name: 'Transpose' },
  ];

  return (
    <div className={`sidebar ${isCollapsed ? 'collapsed' : ''}`}>
      <div className="sidebar-header">
        <h2>Operations</h2>
        <button 
          className="collapse-btn"
          onClick={() => setIsCollapsed(!isCollapsed)}
        >
          {isCollapsed ? '→' : '←'}
        </button>
      </div>
      <div className="operations-list"
      >
        {operations.map((op) => (
          <button
            key={op.id}
            className={`operation-btn ${currentOperation === op.id ? 'active' : ''}`}
            onClick={() => setOperation(op.id)}
          >
            {op.name}
          </button>

        ))}
      </div>
    </div>
  );
};

export default Sidebar;