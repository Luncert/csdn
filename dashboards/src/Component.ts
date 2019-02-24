
import * as React from 'react';

export interface Props {
    style: object;
    children: object[];
}

export class Component extends React.Component<Props, Object> {

    public props: Props;

    constructor(props: Props) {
        super(props);
        this.props = props;
    }

}
