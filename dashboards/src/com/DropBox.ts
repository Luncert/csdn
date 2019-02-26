
import { r, rc } from '../util/react-helper';
import { Props, Component } from './Component';

const styles = <any> require('./DropBox.css');

interface CusProps extends Props {
    color: string;
    name: string;
    selected: number;
    items: string[];
    onChange: (index: number) => void;
}

interface State {
    showList: boolean;
}
/**
 * * props:
 *  * color: 
 *  * name: box名称
 *  * selected: 初始选中
 *  * items: 列表
 *  * onChange: 更换选择
 */
export default class DropBox extends Component {

    props: CusProps;
    state: State;

    constructor(props: CusProps) {
        super(props);
        this.state = {
            showList: false
        };
    }

    render() {
        const { color='gray', name, selected, items=[], onChange, style={} } = this.props;
        const { showList } = this.state;

        let curItem = selected == 0 || selected > 0 ? items[selected] : '';

        let dropList = [];
        items.forEach((item, index) =>
            dropList.push(rc('div', item, {
                className: styles.dropListItem,
                onClick: () => { this.setState({showList: false}); onChange(index); }
            }, item)));

        return r('div', {className: styles.root, style: style},
            rc('a', 'name', { className: styles.name, style: { backgroundColor: color } }, name),
            rc('a', 'item', {
                className: styles.item,
                onClick: () => this.setState({showList: !showList}),
                style: { color: color, borderColor: color } }, 
                showList && rc('div', 'dropList', {className: styles.dropList, style: {color: color}}, dropList), curItem)
        );
    }

}