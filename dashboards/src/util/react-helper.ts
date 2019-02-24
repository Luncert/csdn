
import * as React from 'react';

interface ReactType extends React.DetailedReactHTMLElement<
React.InputHTMLAttributes<HTMLInputElement>, HTMLInputElement> {}

function rc(type: any, key: any, props: object, ...children: Object[]) {
    if (!props) props = {};
    props['key'] = key;
    if (children && children.length > 0) {
        return React.createElement(type, props, children);
    }
    else {
        return React.createElement(type, props);
    }
}

function r(type: any, props: object, ...children: Object[]) {
    if (children && children.length > 0) {
        return React.createElement(type, props, children);
    }
    else {
        return React.createElement(type, props);
    }
}

export {
    ReactType,
    rc,
    r
}