import React, { useState, useEffect }from "react";

const TypingEffect = ({ text, speed = 150 }) => {
    const [displayedText, setDisplayedText] = useState("");
    const [index, setIndex] = useState(0);

    useEffect(() => {

        if (index < text.length) {

        const timer = setTimeout(() => {
            setDisplayedText((prevText) => prevText + text[index]);
            setIndex((prevIndex) => prevIndex + 1);
        }, speed);
        
            return () => clearTimeout(timer);
        }

    }, [index, text, speed]);

    return <span>{displayedText}</span>;
};

export default TypingEffect;