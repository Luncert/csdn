
import { r, rc } from '../util/react-helper';
import { Component, Props } from './Component';

const styles = <any> require('./ItemList.css');

interface CusProps extends Props {
    width: String;
    height: String;
    scrollToBottom: boolean;
}

/**
 * * props
 *  * width
 *  * height
 *  * scrollToBottom
 */
export default class ItemList extends Component {

    props: CusProps;
    root: HTMLElement;

    componentDidMount() {
        if (this.props.scrollToBottom)
            this.scrollToBottom();
    }

    componentDidUpdate() {
        if (this.props.scrollToBottom)
            this.scrollToBottom();
    }

    private scrollToBottom() {
        this.root.scrollTop = this.root.scrollHeight;
    }

    render() {
        const { width, height, style={}, children } = this.props;
        style['width'] = width;
        style['height'] = height;
        return r('div', {
                className: styles.root,
                style: style,
                ref: (node: HTMLElement) => this.root = node
            },
            children
        );
    }

}