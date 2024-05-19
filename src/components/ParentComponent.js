import React, { useState } from 'react';
import Sidebar from './Sidebar'; 

const ParentComponent = () => {
    const [items, setItems] = useState([
        { id: 1, name: 'Product 1', price: 100, quantity: 2 },
        { id: 2, name: 'Product 2', price: 200, quantity: 1 }
    ]);

    const handleRemoveItem = id => {
        setItems(prevItems => prevItems.filter(item => item.id !== id));
    };

    const handleUpdateItemQuantity = (id, newQuantity) => {
        console.log(`Updating item ${id} to quantity ${newQuantity}`); 
        setItems(prevItems => prevItems.map(item =>
            item.id === id ? { ...item, quantity: newQuantity } : item
        ));
    };

    const handleClose = () => {
        console.log('Sidebar closed');
    };

    return (
        <div>
            <Sidebar
                items={items}
                onClose={handleClose}
                onRemoveItem={handleRemoveItem}
                onUpdateItemQuantity={handleUpdateItemQuantity} 
            />
        </div>
    );
};

export default ParentComponent;
