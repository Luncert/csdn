
import { r, rc } from '../util/react-helper';
import { Component, Props } from './Component';

const styles = <any> require('./ItemList.css');

interface CusProps extends Props {
    width: String;
    height: String;
}

export default class ItemList extends Component {

    props: CusProps;

    render() {
        const { width, height, style={}, children } = this.props;
        style['width'] = width;
        style['height'] = height;
        return r('div', {
                className: styles.root,
                style: style
            },
            children
        );
    }

}